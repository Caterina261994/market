package com.market.market.auth;

import com.market.market.auth.service.AuthService;
import com.market.market.auth.service.dto.AuthResponseDto;
import com.market.market.auth.service.dto.UserDto;
import com.market.market.enums.ResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping(value = "signup")
    public Mono<ResponseEntity<?>> requestSignUp(@RequestHeader Map<String, String> headers, @RequestBody UserDto requestBody){
        authService.createCacheUser(requestBody);
        return Mono.fromSupplier(
                () -> ResponseEntity.ok(AuthResponseDto.builder()
                        .total(1)
                        .status(ResponseStatus.SUCCESS)
                        .build()));
    }
    @PostMapping(value = "activateuser")
    public Mono<ResponseEntity<?>> requestSignUp(@RequestHeader Map<String, String> headers, @RequestBody String id){

        return Mono.fromSupplier(
                () -> authService.createKeycloakUser(id));
    }
}
