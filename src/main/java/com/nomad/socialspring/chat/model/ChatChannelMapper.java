package com.nomad.socialspring.chat.model;

import com.nomad.socialspring.chat.dto.ChatChannelResponse;
import com.nomad.socialspring.user.model.User;

public class ChatChannelMapper {
    public static ChatChannelResponse entityToResponse(ChatChannel chatChannel, User currentUser) {
        if (chatChannel == null)
            return null;

        return ChatChannelResponse.builder()
                .id(chatChannel.getId().toString())
                .name(chatChannel.getOtherName(currentUser))
                .build();
    }

    public static ChatChannelResponse entityToResponse(ChatChannel chatChannel) {
        return entityToResponse(chatChannel, null);
    }

}
