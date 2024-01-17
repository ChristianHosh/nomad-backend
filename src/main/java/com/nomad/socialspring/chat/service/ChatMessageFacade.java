package com.nomad.socialspring.chat.service;

import com.nomad.socialspring.chat.dto.ChatMessageRequest;
import com.nomad.socialspring.chat.model.ChatMessageMapper;
import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.model.ChatMessage;
import com.nomad.socialspring.chat.repo.ChatMessageRepository;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageFacade {

    private final ChatMessageRepository repository;

    public ChatMessage newChatMessageFrom(@NotNull ChatMessageRequest request, User sender, ChatChannel chatChannel) {
        return repository.save(ChatMessageMapper.requestToEntity(request.content(), sender, chatChannel));
    }

    public Page<ChatMessage> getMessagesByChatChannel(ChatChannel chatChannel) {
        return getMessagesByChatChannel(chatChannel, 0, 100);
    }

    public Page<ChatMessage> getMessagesByChatChannel(ChatChannel chatChannel, int page, int size) {
        return getMessagesByChatChannel(chatChannel, PageRequest.of(page, size, Sort.by("createdOn")));
    }

    public Page<ChatMessage> getMessagesByChatChannel(ChatChannel chatChannel, Pageable pageable) {
        return repository.findByChatChannel(chatChannel, pageable);
    }
}
