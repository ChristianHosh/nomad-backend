package com.nomad.socialspring.chat.repo;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.model.ChatChannelUser;
import com.nomad.socialspring.chat.model.ChatChannelUsersId;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public ChatChannelUser findById(UUID chatChannelId, Long userId) {
        return findById(new ChatChannelUsersId(chatChannelId, userId));
    }

    public ChatChannelUser findById(ChatChannelUsersId chatChannelUsersId) {
        return repository.findById(chatChannelUsersId)
                .orElseThrow(BxException.xNotFound(ChatChannelUser.class, chatChannelUsersId));
    }

    public void setReadMessage(@NotNull ChatChannelUser chatChannelUser) {
        repository.updateReadMessagesTrueById(chatChannelUser.getId());
    }
}
