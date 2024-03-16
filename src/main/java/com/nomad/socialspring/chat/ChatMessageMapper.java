package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.User;

public class ChatMessageMapper {
  public static ChatMessage requestToEntity(String content, User sender, ChatChannel chatChannel) {
    return ChatMessage.builder()
            .sender(sender)
            .chatChannel(chatChannel)
            .content(content)
            .build();
  }

}
