package com.nomad.socialspring.comment;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserMapper;
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
            .author(UserMapper.entityToResponse(comment.getAuthor(), user))
            .numberOfLikes(comment.getNumberOfLikes())
            .build();
  }
}
