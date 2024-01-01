package com.nomad.socialspring.chat.mapper;

import com.nomad.socialspring.chat.dto.ChatChannelResponse;
import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.user.model.User;
import org.jetbrains.annotations.NotNull;

public class ChatChannelMapper {
    public static ChatChannelResponse entityToResponse(@NotNull ChatChannel chatChannel, User currentUser) {
        return ChatChannelResponse.builder()
                .id(chatChannel.getId().toString())
                .name(chatChannel.getOtherName(currentUser))
                .build();
    }

    public static ChatChannelResponse entityToResponse(@NotNull ChatChannel chatChannel) {
        return entityToResponse(chatChannel, null);
    }

}
