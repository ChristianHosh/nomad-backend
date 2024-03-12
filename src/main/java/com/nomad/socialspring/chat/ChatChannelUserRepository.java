package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.User;
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

  @Query("select count(c) from ChatChannelUser c where c.user = :user and c.readMessages = false")
  long countUnreadMessages(@Param("user") User user);
}