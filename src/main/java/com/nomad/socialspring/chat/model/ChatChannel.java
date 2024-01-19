package com.nomad.socialspring.chat.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.trip.model.Trip;
import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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

    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @OneToMany(mappedBy = "chatChannel", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @Builder.Default
    private Set<ChatChannelUser> chatChannelUsers = new LinkedHashSet<>();

    @Override
    public String getExceptionString() {
        return getId().toString();
    }

    public boolean containsUser(User user) {
        return chatChannelUsers.stream().anyMatch(chatChannelUser -> Objects.equals(chatChannelUser.getChatChannel(), this) && Objects.equals(chatChannelUser.getUser(), user));
    }

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

    public boolean removeUser(User user) {
        return chatChannelUsers.removeIf(channelUser -> channelUser.getUser().equals(user));
    }

    public String getOtherName(User currentUser) {
        if (getChatChannelUsers().size() > 2 || currentUser == null)
            return getName();
        ChatChannelUser otherChannelUser = getChatChannelUsers()
                .stream()
                .filter(chatChannelUser -> !chatChannelUser.getUser().equals(currentUser))
                .findAny().orElse(null);
        if (otherChannelUser == null)
            return getName();
        return otherChannelUser.getUser().getProfile().getDisplayName();
    }

}