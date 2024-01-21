package com.nomad.socialspring.user.dto;

import lombok.Builder;

/**
 * Response DTO for {@link com.nomad.socialspring.user.model.FollowRequest}
 */
@Builder
public record FollowRequestResponse(Long id, UserResponse fromUser) {
}