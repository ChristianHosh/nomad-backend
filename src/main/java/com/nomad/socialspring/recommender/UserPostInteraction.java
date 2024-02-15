package com.nomad.socialspring.recommender;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER_POST_INTERACTION", indexes = {
        @Index(name = "idx_userpostinteraction_postid", columnList = "POST_ID")
})
public class UserPostInteraction extends BaseEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Enumerated
  @Column(name = "EVENT", nullable = false)
  private Event event;

  @ManyToOne
  @JoinColumn(name = "POST_ID")
  private Post post;

  @Column(name = "STRENGTH", nullable = false)
  private Integer strength;


  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    UserPostInteraction userPostInteraction = (UserPostInteraction) o;
    return getId() != null && Objects.equals(getId(), userPostInteraction.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }

  @Override
  public void preSave() {
    super.preSave();
    strength = event.getStrength();
  }
}