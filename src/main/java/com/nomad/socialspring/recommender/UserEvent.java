package com.nomad.socialspring.recommender;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.user.model.User;
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
@Table(name = "T_USER_EVENT")
public class UserEvent extends BaseEntity {
  
  @ManyToOne(optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Enumerated
  @Column(name = "EVENT", nullable = false)
  private Event event;

  @Column(name = "CONTENT_ID", nullable = false)
  private Long contentId;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    UserEvent userEvent = (UserEvent) o;
    return getId() != null && Objects.equals(getId(), userEvent.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}