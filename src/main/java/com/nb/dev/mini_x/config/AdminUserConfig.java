package com.nb.dev.mini_x.config;

import com.nb.dev.mini_x.entities.User;
import com.nb.dev.mini_x.enums.Values;
import com.nb.dev.mini_x.repositories.RoleRepository;
import com.nb.dev.mini_x.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder pwEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder pwEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.pwEncoder = pwEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(Values.ADMIN.name().toLowerCase());
        var userAdmin = userRepository.findByUserName("admin");
        userAdmin.ifPresentOrElse(
                user -> {System.out.print("admin já existe");},

                () -> {
                    var user = new User();
                    user.setUserName("admin");
                    user.setPassword(pwEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
