package com.nb.dev.mini_x.controllers;

import com.nb.dev.mini_x.dtos.request.UserRequest;
import com.nb.dev.mini_x.dtos.response.UserResponse;
import com.nb.dev.mini_x.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/new")
    public ResponseEntity<UserResponse> newUser(@RequestBody @Valid UserRequest userRequest){
        UserResponse newUser = userService.newUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<UserResponse>> listUsers(){
        List<UserResponse> userList = userService.listUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }


}
