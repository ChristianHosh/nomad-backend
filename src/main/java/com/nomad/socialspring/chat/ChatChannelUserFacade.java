package com.nomad.socialspring.chat;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChatChannelUserFacade {

  private final ChatChannelUserRepository repository;

  public void setNewMessageOn(ChatChannel chatChannel) {
    repository.updateReadMessagesFalseByChatChannel(chatChannel);
  }

  public ChatChannelUser findById(@NotNull ChatChannel chatChannel, @NotNull User user) {
    return findById(chatChannel.getId(), user.getId());
  }

  public ChatChannelUser findById(Long chatChannelId, Long userId) {
    return findById(new ChatChannelUsersId(chatChannelId, userId));
  }

  public ChatChannelUser findById(ChatChannelUsersId chatChannelUsersId) {
    return repository.findById(chatChannelUsersId)
            .orElseThrow(BxException.xNotFound(ChatChannelUser.class, chatChannelUsersId));
  }

  public void setReadMessage(@NotNull ChatChannelUser chatChannelUser) {
    repository.updateReadMessagesTrueById(chatChannelUser.getId());
  }

  public ChatChannelUser save(ChatChannelUser chatChannelUser) {
    return repository.save(chatChannelUser);
  }
}
