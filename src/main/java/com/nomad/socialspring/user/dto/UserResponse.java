package com.nomad.socialspring.user.dto;

import com.nomad.socialspring.user.model.Role;
import lombok.Builder;

import java.sql.Timestamp;

/**
 * ResponseOk DTO for {@link com.nomad.socialspring.user.model.User}
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