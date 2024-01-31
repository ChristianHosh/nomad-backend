package com.nomad.socialspring.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for {@link com.nomad.socialspring.review.model.Review}
 */
public record ReviewRequest(
        @Size(max = 500)
        String content,

        @Min(0)
        @Max(5)
        Integer rating
) {
}