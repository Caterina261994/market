package com.market.market.auth.service;

import com.market.market.auth.service.dto.AuthResponseDto;
import com.market.market.auth.service.dto.UserDto;
import com.market.market.conf.EnvConf;
import com.market.market.entity.UserCacheEntity;
import com.market.market.enums.ResponseStatus;
import com.market.market.repo.UserCacheRepository;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private EnvConf envConf;
    @Autowired
    private JavaMailSender emailSender;

    private final String from = "noreply@market.com";

    @Override
    public void createCacheUser(UserDto userDto) {
        UserCacheEntity userCacheEntity = new UserCacheEntity(userDto);
        userCacheEntity = userCacheRepository.save(userCacheEntity);

        String text = "To confirm your registration, please follow the link " +
                userCacheEntity.getUserDTO().getHost() + "/confirm?key=" +
                userCacheEntity.getId();
        sendLetter(from, userDto.getUserEmail(), "confirmation email", text);
    }

    @Override
    public ResponseEntity<AuthResponseDto> createKeycloakUser(String userCacheId) {
        try (
                Keycloak keycloak = KeycloakBuilder.builder()
                        .serverUrl(envConf.getAuthServerUrl())
                        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                        .realm(envConf.getRealm())
                        .clientId(envConf.getClientId())
                        .clientSecret(envConf.getClientSecret())
                        .resteasyClient(
                                new ResteasyClientBuilder().connectionPoolSize(10)
                                        .build()
                        )
                        .build()
        ) {

            keycloak.tokenManager().getAccessToken();

            Optional<UserCacheEntity> userCacheEntity = userCacheRepository.findById(userCacheId);
            if (userCacheEntity.isPresent()) {
                UserDto   userDTO     = userCacheEntity.get().getUserDTO();
                AuthResponseDto rpcResponse = isPresentUser(userDTO, keycloak, userCacheId);
                if (rpcResponse.getErrorMessage() == null) {
                    return ResponseEntity.ok(rpcResponse);
                } else {
                    return ResponseEntity.status(500).body(rpcResponse);
                }
            }
            log.error("Not found user with ID {} in Redis", userCacheId);
            return ResponseEntity.status(404)
                    .body(
                            AuthResponseDto.builder()
                                    .errorCode(404)
                                    .errorMessage("Not found user in Redis")
                                    .status(ResponseStatus.FAILED)
                                    .total(0)
                                    .build()
                    );
        }
    }
    private AuthResponseDto isPresentUser(
            UserDto userDTO,
            Keycloak keycloak,
            String clientId
    )  {
        AuthResponseDto answer;
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getUserName());
        user.setEmail(userDTO.getUserEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        RealmResource realmResource = keycloak.realm(envConf.getRealm());
        UsersResource usersResource = realmResource.users();


        try (Response response = usersResource.create(user)) {

            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);

                log.info("Created userId {}", userId);
                // create password credential
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(userDTO.getUserPassword());

                UserResource userResource = usersResource.get(userId);

                userResource.resetPassword(passwordCred);

                RoleRepresentation realmRoleUser = realmResource.roles()
                        .get(envConf.getDefaultRoleForCPRegistration())
                        .toRepresentation();

                userResource.roles().realmLevel().add(List.of(realmRoleUser));
                answer = AuthResponseDto.builder()
                        .total(1)
                        .status(ResponseStatus.SUCCESS)
                        .build();
                userCacheRepository.deleteById(clientId);
                return answer;
            }
            String  errorMessage = response.readEntity(String.class);
            Pattern pattern      = Pattern.compile("(.+):\"(.+)\"");
            Matcher matcher      = pattern.matcher(errorMessage);
            if (matcher.find()) {
                errorMessage = matcher.group(2);
            } else {
                log.error("Cannot parse error message from keycloak: {}", errorMessage);
            }
            answer = AuthResponseDto.builder()
                    .total(0)
                    .status(ResponseStatus.FAILED)
                    .errorMessage(errorMessage)
                    .errorCode(-32603)
                    .build();

            userCacheRepository.deleteById(clientId);
        }
        return answer;

    }

    private void sendLetter(String from, String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
