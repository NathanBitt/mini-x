package com.nb.dev.mini_x.dtos.response;

import com.nb.dev.mini_x.entities.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String userName, Set<Role> roles) {
}
