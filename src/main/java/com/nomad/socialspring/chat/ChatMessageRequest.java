package com.nomad.socialspring.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(

        @NotNull(message = "token is null")
        @NotBlank(message = "token is blank")
        String jwtToken,

        @NotNull(message = "content is null")
        @NotBlank(message = "content is blank")
        String content,
        @NotNull(message = "chat channel id is null")
        @NotBlank(message = "chat channel id is blank")
        String chatChannelUUID
) {
}
