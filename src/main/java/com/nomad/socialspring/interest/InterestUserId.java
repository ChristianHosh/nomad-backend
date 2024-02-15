package com.nomad.socialspring.interest;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
@Embeddable
public class InterestUserId implements Serializable {

  @NotNull
  @Column(name = "INTEREST_ID", nullable = false)
  private Long interestId;

  @NotNull
  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  @Override
  public final boolean equals(Object object) {
    if (this == object) return true;
    if (object == null) return false;
    Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    InterestUserId that = (InterestUserId) object;
    return getInterestId() != null && Objects.equals(getInterestId(), that.getInterestId())
            && getUserId() != null && Objects.equals(getUserId(), that.getUserId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(interestId, userId);
  }

  @Override
  public String toString() {
    return interestId + " | " + userId;
  }
}
