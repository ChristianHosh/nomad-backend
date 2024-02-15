package com.nomad.socialspring.image;

import lombok.Builder;

/**
 * ResponseOk DTO for {@link Image}
 */
@Builder
public record ImageResponse(Long id, String src) {
}