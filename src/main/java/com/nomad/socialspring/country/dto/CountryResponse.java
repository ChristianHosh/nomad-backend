package com.nomad.socialspring.country.dto;

import lombok.Builder;

/**
 * Response DTO for {@link com.nomad.socialspring.country.model.Country}
 */
@Builder
public record CountryResponse(
        Long id,
        String name
) {
}