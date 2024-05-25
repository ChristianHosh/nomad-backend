package com.nomad.socialspring.trip;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.location.LocationResponse;
import com.nomad.socialspring.user.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

/**
 * Response DTO for {@link Trip}
 */
@Getter
public class TripResponse extends BaseResponse {

  public enum CanJoin {
    CAN_JOIN,
    JOINED,
    DISABLED
  }

  private final Date startDate;
  private final Date endDate;
  private final LocationResponse location;
  private final Integer numberOfParticipants;
  private final CanJoin canJoin;
  private final Boolean hasPassed;
  private final TripUser.TripUserStatus status;

  protected TripResponse(@NotNull Trip entity, User user) {
    super(entity);
    this.startDate = entity.getStartDate();
    this.endDate = entity.getEndDate();
    this.location = LocationResponse.fromEntity(entity.getLocation(), user);
    this.numberOfParticipants = entity.getNumberOfParticipants();
    this.hasPassed = BDate.currentDate().after(entity.getEndDate());

    if (user == null || startDate.before(new Date(System.currentTimeMillis())))
      this.canJoin = CanJoin.DISABLED;
    else if (entity.findTripUser(user) != null)
      this.canJoin = CanJoin.JOINED;
    else
      this.canJoin = CanJoin.CAN_JOIN;

    var tripUser = entity.findTripUser(user);
    this.status = tripUser == null ? null : tripUser.getStatus();
  }

  public static TripResponse fromEntity(Trip trip, User user) {
    if (trip == null) return null;
    return new TripResponse(trip, user);
  }
}