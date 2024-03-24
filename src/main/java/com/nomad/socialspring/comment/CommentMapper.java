package com.nomad.socialspring.comment;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import org.jetbrains.annotations.NotNull;

public class CommentMapper {

  private CommentMapper() {}

  public static Comment requestToEntity(@NotNull CommentRequest commentRequest, User user, Post post) {
    return Comment.builder()
            .author(user)
            .content(commentRequest.content())
            .post(post)
            .build();
  }
}
