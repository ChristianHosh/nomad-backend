package com.nomad.socialspring.trip;

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

  private final Date startDate;
  private final Date endDate;
  private final LocationResponse location;
  private final Integer numberOfParticipants;
  private final Boolean canJoin;

  protected TripResponse(@NotNull Trip entity, User user) {
    super(entity);
    this.startDate = entity.getStartDate();
    this.endDate = entity.getEndDate();
    this.location = LocationResponse.fromEntity(entity.getLocation());
    this.numberOfParticipants = entity.getNumberOfParticipants();
    this.canJoin = user == null ? null : entity.getParticipants().contains(user);
  }

  public static TripResponse fromEntity(Trip trip, User user) {
    if (trip == null) return null;
    return new TripResponse(trip, user);
  }
}