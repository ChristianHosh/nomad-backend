package com.nomad.socialspring.user;

import lombok.Builder;

/**
 * ResponseOk DTO for {@link FollowRequest}
 */
@Builder
public record FollowRequestResponse(Long id, UserResponse fromUser) {
}