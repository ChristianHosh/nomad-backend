package com.nomad.socialspring.chat;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.image.ImageMapper;
import com.nomad.socialspring.user.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
public class ChatChannelResponse extends BaseResponse {

  private final String uuid;
  private final String name;
  private final String avatarUrl;
  private final Boolean isGroup;
  private final Integer groupSize;
  private final Boolean hasUnreadMessages;

  private ChatChannelResponse(@NotNull ChatChannel entity, User user) {
    super(entity);

    uuid = entity.getUuid().toString();
    ChatChannelUser otherUser = null;
    if (user != null && entity.getChatChannelUsers().size() == 2) {
      otherUser = entity.getChatChannelUsers().stream()
              .filter(ccu -> !Objects.equals(ccu.getUser(), user))
              .findAny().orElse(null);
    }

    if (otherUser != null) {
      name = otherUser.getUser().getProfile().getDisplayName();
      avatarUrl = ImageMapper.entityToUrl(otherUser.getUser().getProfile().getProfileImage());
    } else {
      name = entity.getName();
      avatarUrl = null;
    }

    isGroup = otherUser == null;
    groupSize = entity.getChatChannelUsers().size();
    ChatChannelUser currentUser = entity.getChatChannelUsers().stream()
            .filter(ccu -> Objects.equals(ccu.getUser(), user))
            .findAny().orElse(null);
    hasUnreadMessages = currentUser != null && !currentUser.getReadMessages();

  }

  public static ChatChannelResponse fromEntity(ChatChannel entity) {
   return fromEntity(entity, null);
  }

  public static ChatChannelResponse fromEntity(ChatChannel entity, User user) {
    if (entity == null) {
      return null;
    }

    return new ChatChannelResponse(entity, user);
  }
}
