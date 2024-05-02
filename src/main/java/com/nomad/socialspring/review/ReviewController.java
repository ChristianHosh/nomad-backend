package com.nomad.socialspring.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class ReviewController {

  private final ReviewService service;

  @DeleteMapping("/{id}")
  public ReviewResponse deleteReview(
          @PathVariable(name = "id") Long reviewId
  ) {
    return service.deleteReview(reviewId);
  }

  @PutMapping("/{id}")
  public ReviewResponse updateReview(
          @PathVariable(name = "id") Long reviewId,
          @RequestBody ReviewRequest reviewRequest
  ) {
    return service.updateReview(reviewId, reviewRequest);
  }

}
