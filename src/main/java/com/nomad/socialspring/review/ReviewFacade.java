package com.nomad.socialspring.review;

import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewFacade {

  private final ReviewRepository repository;


  public Review save(ReviewRequest reviewRequest, User author, User recipient) {
    return save(Review.builder()
            .content(reviewRequest.content())
            .rating(reviewRequest.rating())
            .author(author)
            .recipient(recipient)
            .build());
  }

  public Review save(Review review) {
    return repository.save(review);
  }
}
