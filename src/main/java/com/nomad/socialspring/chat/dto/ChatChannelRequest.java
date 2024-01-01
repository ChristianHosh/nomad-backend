package com.nomad.socialspring.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ChatChannelRequest(

        String name,

        @NotNull(message = "usernames is null")
        @NotEmpty(message = "usernames is empty")
        List<@NotNull(message = "username is null") @NotBlank(message = "username is blank") String> usernames


) {
}
