package com.market.market.auth.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDto {
    private   String       tokenJWT;
    private   UserDto      user;
    protected String       refreshToken;
}
