package com.market.market.auth;

import com.market.market.auth.service.dto.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthController {

    @PostMapping(value = "signup")
    public Mono<ResponseEntity<?>> requestSignUp(@RequestHeader Map<String, String> headers, @RequestBody UserDto requestBody){
    return null;
    }
}
