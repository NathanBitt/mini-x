package com.nb.dev.mini_x.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
       @NotBlank(message = "defina o nome de usuario")
       @NotNull(message = "defina o campo nome de usuario")
       String userName,

       @NotBlank(message = "defina uma senha")
       @NotNull(message = "defina uma senha")
       String password) {
}
