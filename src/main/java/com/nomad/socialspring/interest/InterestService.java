package com.nomad.socialspring.interest;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.post.PostFacade;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.recommender.PostRecommendationFacade;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestService {

  private final InterestFacade interestFacade;
  private final PostFacade postFacade;
  private final UserFacade userFacade;

  public Page<InterestResponse> getInterests(int page, int size, String name) {
    return interestFacade.getInterests(page, size, name).map(InterestResponse::fromEntity);
  }

  public InterestResponse getInterest(Long id) {
    return interestFacade.getInterest(id).toResponse();
  }

  public Page<PostResponse> getInterestPosts(Long interestId, int page, int size) {
    User user = userFacade.getCurrentUserOrNull();
    Interest interest = interestFacade.getInterest(interestId);

    Page<Post> postPage = postFacade.findByInterest(interest, user, page, size);

    if (user == null)
      return postPage.map(PostResponse::fromEntity);

    return new PageImpl<>(PostRecommendationFacade.sortByInterests(postPage.getContent(), user),
            PageRequest.of(page, size),
            postPage.getTotalElements())
            .map(p -> p.toResponse(user));
  }
}
