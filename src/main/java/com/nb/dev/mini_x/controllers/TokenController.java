package com.nb.dev.mini_x.controllers;

import com.nb.dev.mini_x.dtos.request.LoginRequest;
import com.nb.dev.mini_x.dtos.response.LoginResponse;
import com.nb.dev.mini_x.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {


    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        LoginResponse loginResponse = tokenService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
}