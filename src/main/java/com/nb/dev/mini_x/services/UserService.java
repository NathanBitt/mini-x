package com.nb.dev.mini_x.services;

import com.nb.dev.mini_x.dtos.request.UserRequest;
import com.nb.dev.mini_x.dtos.response.UserResponse;
import com.nb.dev.mini_x.entities.User;
import com.nb.dev.mini_x.enums.Values;
import com.nb.dev.mini_x.repositories.RoleRepository;
import com.nb.dev.mini_x.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder pwEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder pwEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.pwEncoder = pwEncoder;
    }

    public UserResponse newUser(UserRequest userRequest){

        var basicRole = roleRepository.findByName(Values.BASIC.name().toLowerCase());
        var userInPresent = userRepository.findByUserName(userRequest.userName());

        if(userInPresent.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        var newUser = new User();
        newUser.setUserName(userRequest.userName());
        newUser.setPassword(pwEncoder.encode(userRequest.password()));
        newUser.setRoles(Set.of(basicRole));

        userRepository.save(newUser);
        return new UserResponse(newUser.getId(), newUser.getUserName(), newUser.getRoles());
    }

}
