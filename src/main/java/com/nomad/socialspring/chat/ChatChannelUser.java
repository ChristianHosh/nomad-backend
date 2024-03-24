package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_CHAT_CHANNEL_USER")
public class ChatChannelUser implements Serializable {

  @EmbeddedId
  private ChatChannelUsersId id;

  @MapsId("chatChannelId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "CHAT_CHANNEL_ID", nullable = false)
  private ChatChannel chatChannel;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @NotNull
  @Column(name = "READ_MESSAGES", nullable = false)
  @Builder.Default
  private Boolean readMessages = false;

  @Override
  public final boolean equals(Object object) {
    if (this == object) return true;
    if (object == null) return false;
    Class<?> oEffectiveClass = object instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    ChatChannelUser that = (ChatChannelUser) object;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}