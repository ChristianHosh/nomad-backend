package com.nomad.socialspring.post.dto;

import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request DTO for {@link com.nomad.socialspring.post.model.Post}
 */
public record PostRequest(
        @Size(max = 1200)
        String content,

        Boolean isPrivate,

        Set<Long> interestIds

) {
}