package com.nomad.socialspring.comment.dto;

import com.nomad.socialspring.user.dto.UserResponse;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.sql.Timestamp;

/**
 * Response DTO for {@link com.nomad.socialspring.comment.model.Comment}
 */
@Builder
public record CommentResponse(
        Long id,
        Timestamp createdOn,
        Timestamp updatedOn,
        @Size(max = 255)
        String content,
        UserResponse author,
        Integer numberOfLikes
) {
}