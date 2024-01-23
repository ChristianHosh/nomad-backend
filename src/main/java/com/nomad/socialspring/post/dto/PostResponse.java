package com.nomad.socialspring.post.dto;

import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.image.dto.ImageResponse;
import com.nomad.socialspring.user.dto.UserResponse;
import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;

/**
 * ResponseOk DTO for {@link com.nomad.socialspring.post.model.Post}
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