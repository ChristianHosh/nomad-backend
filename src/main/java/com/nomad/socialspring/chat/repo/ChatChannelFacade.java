package com.nomad.socialspring.chat.repo;

import com.nomad.socialspring.chat.model.ChatChannel;
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

    public ChatChannel findById(String id) {
        return findById(UUID.fromString(id));
    }

    public ChatChannel findById(UUID id) {
        return repository.findById(id)
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

    public ChatChannel addNewUsers(String channelId, @NotNull List<User> userList) {
        ChatChannel chatChannel = findById(channelId);
        userList.forEach(chatChannel::addUser);

        return save(chatChannel);
    }


    public ChatChannel removeUsers(String channelId, @NotNull List<User> userList) {
        ChatChannel chatChannel = findById(channelId);
        userList.forEach(chatChannel::removeUser);

        return save(chatChannel);
    }
}
