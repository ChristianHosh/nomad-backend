package com.nomad.socialspring.recommender;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

  private final PostRecommendationFacade recommendationFacade;
  private final UserFacade userFacade;

  //todo implement service
  @GetMapping("/my")
  Page<PostResponse> getReleventPosts() {
    return recommendationFacade.findPostsByRelevance(userFacade.getCurrentUserOrNull(), 0, 25)
        .map(Post::toResponse);
  }

  @GetMapping("/global")
  Page<PostResponse> getGlobalPosts() {
    return recommendationFacade.findPosts(userFacade.getCurrentUserOrNull(), 0, 25)
        .map(Post::toResponse);
  }

  @GetMapping("/trending")
  Page<PostResponse> getTrendingPosts() {
    return recommendationFacade.findPostsByTrendingThisWeek(userFacade.getCurrentUserOrNull(), 0, 25)
        .map(Post::toResponse);
  }
}
