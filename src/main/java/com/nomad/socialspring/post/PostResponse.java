package com.nomad.socialspring.post;

import com.nomad.socialspring.comment.CommentResponse;
import com.nomad.socialspring.image.ImageResponse;
import com.nomad.socialspring.user.UserResponse;
import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;

/**
 * ResponseOk DTO for {@link Post}
 */
@Builder
public record PostResponse(
        Timestamp createdOn,
        Timestamp updatedOn,
        Long id,
        String content,
        Boolean isPrivate,
        Boolean canLike,
        UserResponse author,
        List<ImageResponse> images,
        Integer numberOfLikes,
        CommentResponse topComment
) {
}