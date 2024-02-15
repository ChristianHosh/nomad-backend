package com.nomad.socialspring.interest;

import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER_INTERSEST")
public class UserInterest {

  @EmbeddedId
  private InterestUserId id;

  @MapsId("interestId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "INTEREST_ID", nullable = false)
  private Interest interest;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @NotNull
  @Column(name = "SCORE", nullable = false)
  @Builder.Default
  private Double score = 0.0;

  @NotNull
  @Column(name = "IS_SET_FROM_PROFILE", nullable = false)
  @Builder.Default
  private Boolean isSetFromProfile = false;

  public static UserInterest of(@NotNull Interest interest, @NotNull User user) {
    return UserInterest.builder()
            .id(new InterestUserId(interest.getId(), user.getId()))
            .interest(interest)
            .user(user)
            .build();
  }

  @Override
  public final boolean equals(Object object) {
    if (this == object) return true;
    if (object == null) return false;
    Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    UserInterest that = (UserInterest) object;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}
