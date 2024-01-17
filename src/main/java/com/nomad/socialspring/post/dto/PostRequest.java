package com.nomad.socialspring.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request DTO for {@link com.nomad.socialspring.post.model.Post}
 */
public record PostRequest(
        @Size(max = 1200, message = "content must be equal or less than 1200 characters")
        String content,

        @NotNull
        Boolean isPrivate,

        Set<Long> interestIds

) {
}