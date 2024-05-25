package com.nomad.socialspring.trip;

import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_TRIP_USER")
public class TripUser implements Serializable {

  @EmbeddedId
  private TripUserId id;
  @MapsId("tripId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "TRIP_ID", nullable = false)
  private Trip trip;
  @MapsId("userId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;
  @NotNull
  @Column(name = "STATUS", nullable = false)
  private TripUserStatus status = TripUserStatus.JOINED;

  public enum TripUserStatus {
    JOINED,
    WENT,
    DIDNT_GO
  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  @Embeddable
  public static class TripUserId {

    @NotNull
    @Column(name = "TRIP_ID", nullable = false)
    private Long tripId;

    @NotNull
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
      TripUserId entity = (TripUserId) o;
      return Objects.equals(this.userId, entity.userId) &&
          Objects.equals(this.tripId, entity.tripId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(userId, tripId);
    }

    @Override
    public String toString() {
      return tripId + " | " + userId;
    }
  }

}
