package com.nomad.socialspring.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull(message = "username must not be null")
        @NotBlank(message = "username must not be blank")
        @Size(min = 4, max = 30, message = "username must be between 4 and 30 characters")
        String username,

        @NotNull(message = "password must not be null")
        @NotBlank(message = "password must not be blank")
        @Size(min = 8, max = 40, message = "password must be between 8 and 40 characters long")
        String password
) {
}
