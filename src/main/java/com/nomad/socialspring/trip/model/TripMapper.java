package com.nomad.socialspring.trip.model;

import com.nomad.socialspring.country.model.Country;
import com.nomad.socialspring.trip.dto.TripRequest;
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
