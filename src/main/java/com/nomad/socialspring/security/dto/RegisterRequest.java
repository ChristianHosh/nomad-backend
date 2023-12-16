package com.nomad.socialspring.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull(message = "username must not be null")
        @NotBlank(message = "username must not be blank")
        @Size(min = 4, max = 30, message = "username must be between 4 and 30 characters")
        String username,

        @NotNull(message = "display name must not be null")
        @NotBlank(message = "display name must not be blank")
        @Size(max = 50, message = "display name must be less than 50 characters")
        String displayName,

        @NotNull(message = "email must not be null")
        @NotBlank(message = "email must not be blank")
        @Email(message = "email must be valid")
        @Size(min = 4, max = 50, message = "email must be between 4 and 50 characters long")
        String email,

        @NotNull(message = "password must not be null")
        @NotBlank(message = "password must not be blank")
        @Size(min = 8, max = 40, message = "password must be between 8 and 40 characters long")
        String password
) {
}
