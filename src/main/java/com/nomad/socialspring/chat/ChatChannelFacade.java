package com.nomad.socialspring.chat;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatChannelFacade {

  private final ChatChannelRepository repository;

  public ChatChannel findByUUID(String id) {
    return findByUUID(UUID.fromString(id));
  }

  public ChatChannel findByUUID(UUID id) {
    return repository.findByUuid(id)
            .orElseThrow(BxException.xNotFound(ChatChannel.class, id));
  }

  public ChatChannel newChannel() {
    return repository.save(new ChatChannel());
  }

  public ChatChannel save(ChatChannel chatChannel) {
    return repository.save(chatChannel);
  }

  public ChatChannel newChannel(String name, @NotNull List<User> userList) {
    ChatChannel chatChannel = newChannel();
    chatChannel.setName(name);

    userList.forEach(u -> {
      if (!chatChannel.addUser(u))
        throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_CHANNEL, u);
    });

    return save(chatChannel);
  }

  public ChatChannel addNewUsers(@NotNull ChatChannel chatChannel, @NotNull List<User> userList) {
    userList.forEach(u -> {
      if (!chatChannel.addUser(u))
        throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_CHANNEL, u);
    });

    return save(chatChannel);
  }

  public ChatChannel removeUsers(@NotNull ChatChannel chatChannel, @NotNull List<User> userList) {
    userList.forEach(u -> {
      if (!chatChannel.removeUser(u))
        throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_USER_FROM_CHANNEL, u);
    });

    return save(chatChannel);
  }

  public ChatChannel findByTrip(Trip trip) {
    return repository.findByTrip(trip)
            .orElseThrow(BxException.xNotFound(ChatChannel.class, trip));
  }

  public Page<ChatChannel> getChannelThatContainUser(User user, int page, int size) {
    return repository.findByChatChannelUsers_User(user, PageRequest.of(page, size));
  }
}
