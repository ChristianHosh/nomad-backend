package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserMapper;

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
            .chatChannelId(chatMessage.getChatChannel().getUuid().toString())
            .content(chatMessage.getContent())
            .createdOn(chatMessage.getCreatedOn())
            .sender(UserMapper.entityToResponse(chatMessage.getSender()))
            .build();
  }

}
