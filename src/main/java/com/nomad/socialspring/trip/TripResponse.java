package com.nomad.socialspring.trip;

import com.nomad.socialspring.country.CountryResponse;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Response DTO for {@link Trip}
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