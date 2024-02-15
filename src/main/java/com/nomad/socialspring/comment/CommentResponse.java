package com.nomad.socialspring.comment;

import com.nomad.socialspring.user.UserResponse;
import lombok.Builder;

import java.sql.Timestamp;

/**
 * ResponseOk DTO for {@link Comment}
 */
@Builder
public record CommentResponse(
        Long id,
        Timestamp createdOn,
        Timestamp updatedOn,
        String content,
        Boolean canLike,
        UserResponse author,
        Integer numberOfLikes
) {
}