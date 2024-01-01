package com.nomad.socialspring.chat.dto;

import lombok.Builder;

@Builder
public record ChatChannelResponse(
        String id,
        String name
) {
}
