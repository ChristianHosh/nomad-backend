package com.nomad.socialspring.trip;

import com.nomad.socialspring.country.Country;
import org.jetbrains.annotations.NotNull;

public class TripMapper {

  public static Trip requestToEntity(@NotNull TripRequest request, Country country) {
    return Trip.builder()
            .country(country)
            .startDate(request.startDate())
            .endDate(request.endDate())
            .build();
  }
}
