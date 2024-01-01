package com.nomad.socialspring.chat.mapper;

import com.nomad.socialspring.chat.dto.ChatMessageResponse;
import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.model.ChatMessage;
import com.nomad.socialspring.user.mapper.UserMapper;
import com.nomad.socialspring.user.model.User;

public class ChatMessageMapper {
    public static ChatMessage requestToEntity(String content, User sender, ChatChannel chatChannel) {
        return ChatMessage.builder()
                .sender(sender)
                .chatChannel(chatChannel)
                .content(content)
                .build();
    }

    public static ChatMessageResponse entityToResponse(ChatMessage chatMessage) {
        if (chatMessage == null)
            return null;

        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .chatChannelId(chatMessage.getChatChannel().getId().toString())
                .content(chatMessage.getContent())
                .createdOn(chatMessage.getCreatedOn())
                .sender(UserMapper.entityToResponse(chatMessage.getSender()))
                .build();
    }

}
