package com.nomad.socialspring.chat.repo;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.model.ChatChannelUser;
import com.nomad.socialspring.chat.model.ChatChannelUsersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChatChannelUserRepository extends JpaRepository<ChatChannelUser, ChatChannelUsersId> {
    @Transactional
    @Modifying
    @Query("UPDATE ChatChannelUser c " +
            "SET c.readMessages = FALSE " +
            "WHERE c.chatChannel = :chatChannel")
    void updateReadMessagesFalseByChatChannel(@Param("chatChannel") ChatChannel chatChannel);

    @Transactional
    @Modifying
    @Query("UPDATE ChatChannelUser c " +
            "SET c.readMessages = TRUE " +
            "WHERE c.id = :id")
    void updateReadMessagesTrueById(@Param("id") ChatChannelUsersId id);
}