package com.nomad.socialspring.trip;

import com.nomad.socialspring.location.LocationResponse;

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
        LocationResponse country,
        Integer numberOfParticipants
) {
}