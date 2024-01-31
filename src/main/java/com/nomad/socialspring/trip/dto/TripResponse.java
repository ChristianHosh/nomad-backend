package com.nomad.socialspring.trip.dto;

import com.nomad.socialspring.country.dto.CountryResponse;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Response DTO for {@link com.nomad.socialspring.trip.model.Trip}
 */
public record TripResponse(
        Long id,
        Timestamp createdOn,
        Timestamp updatedOn,
        Date startDate,
        Date endDate,
        CountryResponse country,
        Integer numberOfParticipants
) {
}