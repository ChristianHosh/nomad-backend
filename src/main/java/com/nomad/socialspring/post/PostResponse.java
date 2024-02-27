package com.nomad.socialspring.post;

import com.nomad.socialspring.comment.CommentResponse;
import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.image.ImageMapper;
import com.nomad.socialspring.image.ImageResponse;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserResponse;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class PostResponse extends BaseResponse {
  private final String content;
  private final Boolean isPrivate;
  private final Boolean canLike;
  private final UserResponse author;
  private final List<ImageResponse> images;
  private final Integer numberOfLikes;
//  private final TripResponse trip;
  private final CommentResponse topComment;
  
  private PostResponse(@NotNull Post post, User user) {
    super(post);
    content = post.getContent();
    isPrivate = post.getIsPrivate();
    author = post.getAuthor().toResponse(user);
    images = post.getImages().stream().map(ImageMapper::entityToResponse).toList();
    numberOfLikes = post.getNumberOfLikes();
    topComment = post.getTopComment().toResponse(user);
    canLike = user == null ? null : post.getLikes().contains(user);
    if (post.getTrip() != null) {
      // do trip work
    }
  }
  
  public static PostResponse fromEntity(Post post) {
    return fromEntity(post, null); 
  }
  
  public static PostResponse fromEntity(Post post, User user) {
    return post == null ? null : new PostResponse(post, user);
  }
}
