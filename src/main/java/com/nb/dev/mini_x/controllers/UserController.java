package com.nb.dev.mini_x.controllers;

import com.nb.dev.mini_x.dtos.request.UserRequest;
import com.nb.dev.mini_x.dtos.response.UserResponse;
import com.nb.dev.mini_x.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/new")
    public ResponseEntity<UserResponse> newUser(@RequestBody UserRequest userRequest){
        var newUser = userService.newUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
