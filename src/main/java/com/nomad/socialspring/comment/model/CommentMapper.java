package com.nomad.socialspring.comment.model;

import com.nomad.socialspring.comment.dto.CommentRequest;
import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.model.UserMapper;
import org.jetbrains.annotations.NotNull;

public class CommentMapper {
    public static Comment requestToEntity(@NotNull CommentRequest commentRequest, User user, Post post) {
        return Comment.builder()
                .author(user)
                .content(commentRequest.content())
                .post(post)
                .build();
    }

    public static CommentResponse entityToResponse(Comment comment) {
        return entityToResponse(comment, null);
    }

    public static CommentResponse entityToResponse(Comment comment, User user) {
        if (comment == null)
            return null;

        return CommentResponse.builder()
                .id(comment.getId())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .content(comment.getContent())
                .canLike(user != null && comment.getLikes().contains(user))
                .author(UserMapper.entityToResponse(comment.getAuthor()))
                .numberOfLikes(comment.getNumberOfLikes())
                .build();
    }
}
