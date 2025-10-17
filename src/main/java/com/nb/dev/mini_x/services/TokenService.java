package com.nb.dev.mini_x.services;

import com.nb.dev.mini_x.dtos.request.LoginRequest;
import com.nb.dev.mini_x.dtos.response.LoginResponse;
import com.nb.dev.mini_x.entities.Role;
import com.nb.dev.mini_x.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.stream.Collectors;

@Service
public class TokenService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder pwEncoder;
    private final JwtEncoder jwtEncoder;

    public TokenService(UserRepository userRepository, BCryptPasswordEncoder pwEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.pwEncoder = pwEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse login (LoginRequest loginRequest){
        var user = userRepository.findByUserName(loginRequest.userName());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, pwEncoder)){
            throw new BadCredentialsException("user or password invalid");
        }
        Instant now = Instant.now();
        long expiresIn = 300L;
        String scopes = user
                .get()
                .getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("mini-x backend")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, expiresIn);



    }
}
