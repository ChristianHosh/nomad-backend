package com.nomad.socialspring.user;

import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.List;

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
    Long locationId,
    List<Long> interestsIds

) {
}