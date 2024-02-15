package com.nomad.socialspring.user;

import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.Set;

/**
 * Request DTO for {@link Profile}
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