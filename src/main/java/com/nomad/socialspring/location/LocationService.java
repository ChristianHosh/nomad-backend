package com.nomad.socialspring.location;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.post.PostFacade;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.recommender.PostRecommendationFacade;
import com.nomad.socialspring.review.Review;
import com.nomad.socialspring.review.ReviewFacade;
import com.nomad.socialspring.review.ReviewRequest;
import com.nomad.socialspring.review.ReviewResponse;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final LocationFacade locationFacade;
  private final PostFacade postFacade;
  private final UserFacade userFacade;
  private final ReviewFacade reviewFacade;


  public Page<LocationResponse> getLocations(int page, int size, String query) {
    return locationFacade
        .getLocations(page, size, query)
        .map(LocationResponse::fromEntity);
  }

  public LocationResponse getLocation(Long id) {
    return locationFacade.findById(id).toResponse(userFacade.getCurrentUserOrNull());
  }

  public Page<PostResponse> getTrips(Long id, int page, int size) {
    User user = userFacade.getCurrentUserOrNull();

    Page<Post> postPage = postFacade.findByLocation(locationFacade.findById(id), user, page, size);

    if (user == null)
      return postPage.map(Post::toResponse);

    return new PageImpl<>(PostRecommendationFacade.sortByInterests(postPage.getContent(), user),
        PageRequest.of(page, size), postPage.getTotalElements())
        .map(p -> p.toResponse(user));
  }

  @Transactional
  public ReviewResponse createReview(Long locationId, ReviewRequest reviewRequest) {
    User user = userFacade.getCurrentUser();
    Location location = locationFacade.findById(locationId);

    Review review = reviewFacade.findByLocationAndAuthor(location, user);
    if (review == null) {
      review = new Review();
      review.setAuthor(user);
      review.setLocation(location);
    }
    review.setRating(reviewRequest.rating());
    review.setContent(reviewRequest.content());
    return reviewFacade.save(review).toResponse();
  }

  public Page<ReviewResponse> getReviews(Long locationId, int page, int size) {
    return reviewFacade.findByLocation(locationFacade.findById(locationId), page, size)
        .map(Review::toResponse);
  }
}
