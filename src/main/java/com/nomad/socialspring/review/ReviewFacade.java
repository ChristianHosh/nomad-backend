package com.nomad.socialspring.review;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

  public Page<Review> findByUser(User user, int page, int size) {
    return repository.findByRecipient(user, PageRequest.of(page, size));
  }

  public Review findById(Long id) {
    return repository.findById(id).orElseThrow(BxException.xNotFound(Review.class, id));
  }

  public Review delete(Review review) {
    repository.delete(review);
    return review;
  }
}
