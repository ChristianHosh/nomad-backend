package com.nomad.socialspring.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ChatChannelRequest(

        String name,

        @NotNull(message = "id is null")
        @NotEmpty(message = "id is empty")
        List<@NotNull(message = "id is null")Long> userIds


) {
}
