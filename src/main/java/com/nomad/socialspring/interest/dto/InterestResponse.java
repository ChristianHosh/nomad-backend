package com.nomad.socialspring.interest.dto;

import lombok.Builder;

/**
 * DTO for {@link com.nomad.socialspring.interest.model.Interest}
 */
@Builder
public record InterestResponse(Long id, String name) {
}