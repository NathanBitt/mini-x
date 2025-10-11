package com.nb.dev.mini_x.repositories;

import com.nb.dev.mini_x.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
