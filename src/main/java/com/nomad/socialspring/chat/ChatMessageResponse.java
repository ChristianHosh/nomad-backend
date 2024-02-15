package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.UserResponse;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record ChatMessageResponse(

        Long id,
        String chatChannelId,
        String content,
        Timestamp createdOn,
        UserResponse sender
) {
}
