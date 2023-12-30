package com.nomad.socialspring.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ChatChannelUsersId implements Serializable {
    @Serial
    private static final long serialVersionUID = -691292451832060807L;

    @NotNull
    @Column(name = "chat_channel_id", nullable = false)
    private UUID chatChannelId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatChannelUsersId entity = (ChatChannelUsersId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.chatChannelId, entity.chatChannelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatChannelId);
    }

}