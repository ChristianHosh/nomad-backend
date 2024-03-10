package com.nomad.socialspring.user;

import com.nomad.socialspring.location.LocationResponse;
import lombok.Builder;

import java.sql.Date;

/**
 * ResponseOk DTO for {@link Profile}
 */
@Builder
public record ProfileResponseR(
        String displayName,
        String bio,
        Gender gender,
        Date birthDate,
        String profileImageUrl,
        LocationResponse country,
        Integer numberOfFollowers,
        Integer numberOfFollowings
) {
}