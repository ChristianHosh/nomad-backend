package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageFacade {

  private final ChatMessageRepository repository;

  public ChatMessage newChatMessageFrom(@NotNull ChatMessageRequest request, User sender, ChatChannel chatChannel) {
    return repository.save(ChatMessageMapper.requestToEntity(request.content(), sender, chatChannel));
  }

  public Page<ChatMessage> getMessagesByChatChannel(ChatChannel chatChannel, int page, int size) {
    return repository.findByChatChannel(chatChannel, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdOn"))));
  }
}
