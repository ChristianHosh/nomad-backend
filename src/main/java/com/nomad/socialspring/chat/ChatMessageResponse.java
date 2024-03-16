package com.nomad.socialspring.chat;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.user.UserResponse;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class ChatMessageResponse extends BaseResponse {

  private final String chatChannelUUID;
  private final String content;
  private final UserResponse sender;

  private ChatMessageResponse(@NotNull ChatMessage entity) {
    super(entity);
    this.content = entity.getContent();
    this.sender = entity.getSender().toResponse();
    this.chatChannelUUID = entity.getChatChannel().getUuid().toString();
  }

  public static ChatMessageResponse fromEntity(ChatMessage entity) {
    return entity == null ? null : new ChatMessageResponse(entity);
  }

}
