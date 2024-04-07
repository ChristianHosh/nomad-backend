package com.nomad.socialspring.recommender;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.post.PostRepository;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostRecommendationFacade {

  private final UserPostInteractionRepository userPostInteractionRepository;
  private final PostRepository postRepository;

  public Page<PostResponse> findPosts(User currentUser, int page, int size) {
    Page<Post> postPage = postRepository.findPosts(currentUser, PageRequest.of(page, size));

    if (currentUser == null)
      return postPage.map(Post::toResponse);

    List<Post> postList = postPage.getContent();

    return postPage.map(p -> p.toResponse(currentUser));
  }


}
