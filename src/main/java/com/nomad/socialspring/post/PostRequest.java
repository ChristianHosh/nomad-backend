package com.nomad.socialspring.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Request DTO for {@link Post}
 */
public record PostRequest(
        @Size(max = 1200, message = "content must be equal or less than 1200 characters")
        String content,

        @NotNull(message = "isPrivate can't be null")
        Boolean isPrivate,

        @NotNull(message = "interestIds can't be null")
        List<Long> interestsIds,

        String startDate,

        String endDate,

        Long locationId
) {
}