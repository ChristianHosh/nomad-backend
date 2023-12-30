package com.nomad.socialspring.chat.model;

import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_chat_channel_users")
public class ChatChannelUser {

    @EmbeddedId
    private ChatChannelUsersId id;

    @MapsId("chatChannelId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_channel_id", nullable = false)
    private ChatChannel chatChannel;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "read_messages", nullable = false)
    @Builder.Default
    private Boolean readMessages = false;

}