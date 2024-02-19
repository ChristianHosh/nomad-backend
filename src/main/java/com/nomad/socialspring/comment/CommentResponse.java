package com.nomad.socialspring.comment;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserResponse;
import lombok.Getter;

@Getter
public class CommentResponse extends BaseResponse {

  private final String content;
  private final UserResponse author;
  private final Boolean canLike;
  private final Integer numberOfLikes;

  private CommentResponse(Comment comment, User user) {
    super(comment);

    content = comment.getContent();
    author = comment.getAuthor().toResponse(user);
    canLike = user == null ? null : comment.getLikes().contains(user);
    numberOfLikes = comment.getNumberOfLikes();
  }
  
  public static CommentResponse fromEntity(Comment comment) {
    return fromEntity(comment, null);
  }
  
  public static CommentResponse fromEntity(Comment comment, User other) {
    return comment == null ? null : new CommentResponse(comment, other);
  }
}
