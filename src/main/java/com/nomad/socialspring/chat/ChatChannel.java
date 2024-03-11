package com.nomad.socialspring.chat;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_CHAT_CHANNEL")
public class ChatChannel extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "UUID", nullable = false)
  private UUID uuid;

  @Column(name = "NAME")
  private String name;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  @JoinColumn(name = "TRIP_ID")
  private Trip trip;

  @OneToMany(mappedBy = "chatChannel", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  @Builder.Default
  private Set<ChatChannelUser> chatChannelUsers = new HashSet<>();

  @OneToMany(mappedBy = "chatChannel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<ChatMessage> chatMessages = new LinkedHashSet<>();

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean containsUser(User user) {
    return chatChannelUsers.stream().anyMatch(chatChannelUser -> Objects.equals(chatChannelUser.getChatChannel(), this) && Objects.equals(chatChannelUser.getUser(), user));
  }

  public ChatChannelUser findUser(User user) {
    return chatChannelUsers.stream()
            .filter(channelUser -> channelUser.getUser().equals(user))
            .findAny().orElse(null);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean addUser(User user) {
    return chatChannelUsers.add(newChatChannelUser(user));
  }

  public ChatChannelUser newChatChannelUser(User user) {
    return new ChatChannelUser(
            new ChatChannelUsersId(getId(), user.getId()),
            this,
            user,
            false
    );
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean removeUser(User user) {
    return chatChannelUsers.removeIf(channelUser -> channelUser.getUser().equals(user));
  }

  public ChatChannelResponse toResponse() {
    return ChatChannelResponse.fromEntity(this);
  }

  public ChatChannelResponse toResponse(User user) {
    return ChatChannelResponse.fromEntity(this, user);
  }

}