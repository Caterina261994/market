package com.market.market.conf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AuthConf {
    @Value("${AUTH_SERVER_URL}")
    private       String authServerUrl;
    @Value("${REALM}")
    private       String realm;
    @Value("${CLIENT_ID}")
    private       String clientId;
    @Value("${CLIENT_SECRET}")
    private       String clientSecret;
    @Value("${CLIENT_ADMIN_USERNAME}")
    private       String clientAdminUsername;
    @Value("${CLIENT_ADMIN_PASSWORD}")
    private       String clientAdminPassword;
    private final String defaultRoleForCPRegistration = "cp.client";
}
