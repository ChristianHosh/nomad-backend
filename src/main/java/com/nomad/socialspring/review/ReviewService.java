package com.nomad.socialspring.review;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewFacade reviewFacade;
  private final UserFacade userFacade;

  @Transactional
  public ReviewResponse deleteReview(Long reviewId) {
    User currentUser = userFacade.getCurrentUser();
    Review review = reviewFacade.findById(reviewId);

    if (review.getAuthor().equals(currentUser))
      return reviewFacade.delete(review).toResponse();
    throw BxException.unauthorized(currentUser);
  }

  @Transactional
  public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest) {
    User currentUser = userFacade.getCurrentUser();
    Review review = reviewFacade.findById(reviewId);
    if (review.getAuthor().equals(currentUser)) {
      review.setContent(reviewRequest.content());
      review.setRating(reviewRequest.rating());
      return reviewFacade.save(review).toResponse();
    }
    throw BxException.unauthorized(currentUser);
  }
}
