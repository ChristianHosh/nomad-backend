package com.nomad.socialspring.trip;

import com.nomad.socialspring.location.Location;
import org.jetbrains.annotations.NotNull;

public class TripMapper {

  public static Trip requestToEntity(@NotNull TripRequest request, Location location) {
    return Trip.builder()
            .location(location)
            .startDate(request.startDate())
            .endDate(request.endDate())
            .build();
  }
}
