package com.market.market.conf;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
public class EnvConf {
    //Keycloak
    private       String authServerUrl;
    private       String realm;
    private       String clientId;
    private       String clientSecret;
    private       String clientAdminUsername;
    private       String clientAdminPassword;
    private final String defaultRoleForCPRegistration = "market.client";
    //EmailService
    private String emailHost;
    private Integer emailPort;
    private String emailUserName;
    private String emailPassword;
}
