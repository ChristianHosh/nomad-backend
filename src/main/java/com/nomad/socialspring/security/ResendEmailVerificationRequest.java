package com.nomad.socialspring.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResendEmailVerificationRequest(

    @NotNull(message = "email must not be null")
    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    @Size(min = 4, max = 50, message = "email must be between 4 and 50 characters long")
    String email

) {

}
