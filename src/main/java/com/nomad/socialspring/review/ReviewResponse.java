package com.nomad.socialspring.review;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.user.UserResponse;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class ReviewResponse extends BaseResponse {

  private final String content;
  private final Integer rating;
  private final UserResponse author;

  protected ReviewResponse(@NotNull Review entity) {
    super(entity);
    this.content = entity.getContent();
    this.rating = entity.getRating();
    this.author = entity.getAuthor().toResponse();
  }
}
