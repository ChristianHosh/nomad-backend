package com.nomad.socialspring.chat;

import lombok.Builder;

@Builder
public record ChatChannelResponse(
        String id,
        String name
) {
}
