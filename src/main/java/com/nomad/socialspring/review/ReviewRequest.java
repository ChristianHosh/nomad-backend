package com.nomad.socialspring.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for {@link Review}
 */
public record ReviewRequest(
        @Size(max = 500)
        String content,

        @Min(0)
        @Max(5)
        Integer rating
) {
}