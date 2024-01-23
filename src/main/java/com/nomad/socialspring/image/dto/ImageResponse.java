package com.nomad.socialspring.image.dto;

import lombok.Builder;

/**
 * ResponseOk DTO for {@link com.nomad.socialspring.image.model.Image}
 */
@Builder
public record ImageResponse(Long id, String src) {
}