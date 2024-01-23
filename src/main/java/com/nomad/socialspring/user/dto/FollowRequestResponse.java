package com.nomad.socialspring.user.dto;

import lombok.Builder;

/**
 * ResponseOk DTO for {@link com.nomad.socialspring.user.model.FollowRequest}
 */
@Builder
public record FollowRequestResponse(Long id, UserResponse fromUser) {
}