package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.User;

public class ChatChannelMapper {
    public static ChatChannelResponse entityToResponse(ChatChannel chatChannel, User currentUser) {
        if (chatChannel == null)
            return null;

        return ChatChannelResponse.builder()
                .id(chatChannel.getUuid().toString())
                .name(chatChannel.getOtherName(currentUser))
                .build();
    }

    public static ChatChannelResponse entityToResponse(ChatChannel chatChannel) {
        return entityToResponse(chatChannel, null);
    }

}
