package com.market.market.auth.service;

import com.market.market.auth.service.dto.AuthResponseDto;
import com.market.market.auth.service.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    void createCacheUser(UserDto userDto);
    ResponseEntity<AuthResponseDto> createKeycloakUser(String userCacheId);
}
