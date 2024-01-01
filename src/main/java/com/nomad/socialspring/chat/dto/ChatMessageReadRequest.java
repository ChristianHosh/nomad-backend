package com.nomad.socialspring.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageReadRequest(

        @NotNull(message = "chat channel id is null")
        @NotBlank(message = "chat channel id is blank")
        String chatChannelId
) {
}
