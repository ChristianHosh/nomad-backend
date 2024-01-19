package com.nomad.socialspring.chat.service;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.repo.ChatChannelRepository;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

        userList.forEach(chatChannel::addUser);

        return save(chatChannel);
    }

    public ChatChannel addNewUsers(@NotNull ChatChannel chatChannel, @NotNull List<User> userList) {
        userList.forEach(chatChannel::addUser);

        return save(chatChannel);
    }

    public ChatChannel removeUsers(@NotNull ChatChannel chatChannel, @NotNull List<User> userList) {
        userList.forEach(chatChannel::removeUser);

        return save(chatChannel);
    }
}
