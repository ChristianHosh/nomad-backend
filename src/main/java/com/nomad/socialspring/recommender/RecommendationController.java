package com.nomad.socialspring.recommender;

import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

  private final PostRecommendationFacade recommendationFacade;
  private final UserFacade userFacade;

  @GetMapping("")
  public Page<PostResponse> getRecommendations(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return recommendationFacade.findPosts(userFacade.getCurrentUserOrNull(), page, size);
  }

  @GetMapping("/followings")
  public Page<PostResponse> getFollowingsRecommendations(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return recommendationFacade.findFollowingsRecommendations(userFacade.getCurrentUser(), page, size);
  }

  @GetMapping("/local-trips")
  public Page<PostResponse> getLocalTripsRecommendations(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return recommendationFacade.findLocalTripsRecommendations(userFacade.getCurrentUser(), page, size);
  }
}
