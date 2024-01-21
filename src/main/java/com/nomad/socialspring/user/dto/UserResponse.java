package com.nomad.socialspring.user.dto;

import com.nomad.socialspring.user.model.Role;
import lombok.Builder;

/**
 * Response DTO for {@link com.nomad.socialspring.user.model.User}
 */
@Builder
public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        String token,
        Boolean canFollow,
        ProfileResponse profile
) {
}