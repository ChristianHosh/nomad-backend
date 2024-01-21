package com.nomad.socialspring.user.dto;

import com.nomad.socialspring.user.model.Gender;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.Set;

/**
 * DTO for {@link com.nomad.socialspring.user.model.Profile}
 */
public record ProfileRequest(
        @Size(max = 50)
        String displayName,
        @Size(max = 600)
        String bio,
        Gender gender,
        Date birthDate,
        Long countryId,
        Set<String> interestsTags

) {
}