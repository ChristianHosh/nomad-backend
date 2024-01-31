package com.nomad.socialspring.trip.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

/**
 * Request DTO for {@link com.nomad.socialspring.trip.model.Trip}
 */
public record TripRequest(

        @FutureOrPresent(message = "startDate can't be in the past")
        @NotNull(message = "startDate can't be null")
        Date startDate,

        @FutureOrPresent(message = "endDate can't be in the past")
        @NotNull(message = "endDate can't be null")
        Date endDate,

        @NotNull(message = "countryId can't be null")
        Long countryId
) {
}