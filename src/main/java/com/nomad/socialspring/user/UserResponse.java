package com.nomad.socialspring.user;

import lombok.Builder;

import java.sql.Timestamp;

/**
 * ResponseOk DTO for {@link User}
 */
@Builder
public record UserResponse(
        Long id,
        Timestamp updatedOn,
        Timestamp createdOn,
        String username,
        String email,
        Role role,
        String token,
        FollowStatus followStatus,
        Integer rating,
        ProfileResponse profile
) {
}