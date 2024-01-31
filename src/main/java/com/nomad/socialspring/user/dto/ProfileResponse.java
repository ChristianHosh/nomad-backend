package com.nomad.socialspring.user.dto;

import com.nomad.socialspring.country.dto.CountryResponse;
import com.nomad.socialspring.user.model.Gender;
import lombok.Builder;

import java.sql.Date;

/**
 * ResponseOk DTO for {@link com.nomad.socialspring.user.model.Profile}
 */
@Builder
public record ProfileResponse(
        String displayName,
        String bio,
        Gender gender,
        Date birthDate,
        String profileImageUrl,
        CountryResponse country,
        Integer numberOfFollowers,
        Integer numberOfFollowings
) {
}