package com.nomad.socialspring.chat;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.image.ImageMapper;
import com.nomad.socialspring.user.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
public class ChatChannelResponse extends BaseResponse {

  private final String uuid;
  private final String avatarUrl;
  private final Boolean isGroup;
  private final Integer groupSize;
  private final Boolean hasUnreadMessages;
  private final ChatMessageResponse latestMessage;
  private String name;

  private ChatChannelResponse(@NotNull ChatChannel entity, User user) {
    super(entity);

    uuid = entity.getUuid().toString();
    ChatChannelUser otherUser = null;

    Set<ChatChannelUser> channelUserSet = entity.getChatChannelUsers();
    groupSize = channelUserSet.size();
    isGroup = groupSize > 2;

    if (Boolean.FALSE.equals(isGroup) && user != null) {
      otherUser = channelUserSet.stream()
              .filter(ccu -> !Objects.equals(ccu.getUser(), user))
              .findAny().orElse(null);
    }

    if (otherUser != null) {
      if (entity.getName() == null)
        name = otherUser.getUser().getProfile().getDisplayName();
      else
        name = entity.getName();
      avatarUrl = ImageMapper.entityToUrl(otherUser.getUser().getProfile().getProfileImage());
    } else {
      if (entity.getName() == null) {
        List<String> names = channelUserSet.stream()
                .map(ChatChannelUser::getUser)
                .filter(u -> !Objects.equals(u, user))
                .map(u -> u.getProfile().getDisplayName())
                .limit(3)
                .toList();
        name = String.join(", ", names);
        if (groupSize > 3)
          name += ",...";
      } else {
        name = entity.getName();
      }
      if (entity.getTrip() != null) {
        Image image = entity.getTrip().getPost().getImages().stream().findFirst().orElse(null);
        avatarUrl = ImageMapper.entityToUrl(image);
      } else {
        avatarUrl = null;
      }

    }

    ChatChannelUser currentUser = channelUserSet.stream()
            .filter(ccu -> Objects.equals(ccu.getUser(), user))
            .findAny().orElse(null);
    if (currentUser != null)
      hasUnreadMessages = !currentUser.getReadMessages();
    else
      hasUnreadMessages = false;

    ChatMessage latest = entity.getChatMessages().stream()
            .max(Comparator.comparingLong(c -> c.getCreatedOn().getTime()))
            .orElse(null);
    latestMessage = latest == null ? null : latest.toResponse();
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
