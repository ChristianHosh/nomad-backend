package com.nomad.socialspring.post;

import com.nomad.socialspring.comment.CommentMapper;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.image.ImageMapper;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserMapper;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PostMapper {

  public static PostResponse entityToResponse(Post post) {
    return entityToResponse(post, null);
  }

  public static PostResponse entityToResponse(Post post, User user) {
    if (post == null)
      return null;

    return PostResponse.builder()
            .createdOn(post.getCreatedOn())
            .updatedOn(post.getUpdatedOn())
            .id(post.getId())
            .content(post.getContent())
            .isPrivate(post.getIsPrivate())
            .canLike(user != null && post.getLikes().contains(user))
            .images(post.getImages().stream().map(ImageMapper::entityToResponse).toList())
            .author(UserMapper.entityToResponse(post.getAuthor(), user))
            .numberOfLikes(post.getNumberOfLikes())
            .topComment(CommentMapper.entityToResponse(post.getTopComment()))
            .build();
  }

  @NotNull
  public static Post requestToEntity(@NotNull PostRequest request, Trip trip, User user, Set<Interest> interestSet, Set<Image> images) {
    Post post = Post.builder()
            .author(user)
            .content(request.content())
            .isPrivate(request.isPrivate())
            .interests(interestSet)
            .trip(trip)
            .images(images)
            .build();
    post.getImages().forEach(image -> image.setPost(post));
    return post;
  }
}
