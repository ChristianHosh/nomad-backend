package com.nomad.socialspring.chat.repo;

import com.nomad.socialspring.chat.model.ChatChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, UUID> {
}