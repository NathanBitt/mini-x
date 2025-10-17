package com.nb.dev.mini_x.services;

import com.nb.dev.mini_x.dtos.request.UserRequest;
import com.nb.dev.mini_x.dtos.response.UserResponse;
import com.nb.dev.mini_x.entities.Role;
import com.nb.dev.mini_x.entities.User;
import com.nb.dev.mini_x.enums.Values;
import com.nb.dev.mini_x.exceptions.UserNameUnavailableException;
import com.nb.dev.mini_x.repositories.RoleRepository;
import com.nb.dev.mini_x.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Role basicRole = roleRepository.findByName(Values.BASIC.name().toLowerCase());
        var userNameInDb = userRepository.findByUserName(userRequest.userName());

        if(userNameInDb.isPresent()) throw new UserNameUnavailableException("o nome " + userRequest.userName() + " já está em uso");

        User newUser = new User();
        newUser.setUserName(userRequest.userName());
        newUser.setPassword(pwEncoder.encode(userRequest.password()));
        newUser.setRoles(Set.of(basicRole));

        userRepository.save(newUser);
        return new UserResponse(newUser.getId(), newUser.getUserName(), newUser.getRoles());
    }

    public List<UserResponse> listUsers(){
        List<User> userList = userRepository.findAll();

        return userList.stream().map(
                user -> new UserResponse(
                        user.getId(),
                        user.getUserName(),
                        user.getRoles())
        ).toList();
    }

}
