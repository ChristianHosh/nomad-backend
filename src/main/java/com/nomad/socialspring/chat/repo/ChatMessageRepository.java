package com.nomad.socialspring.chat.repo;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT c FROM ChatMessage c " +
            "WHERE c.chatChannel = :chatChannel")
    Page<ChatMessage> findByChatChannel(@Param("chatChannel") ChatChannel chatChannel, Pageable pageable);
}