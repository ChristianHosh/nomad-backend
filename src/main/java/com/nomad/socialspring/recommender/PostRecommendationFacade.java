package com.nomad.socialspring.recommender;

import com.nomad.socialspring.country.Country;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PostRecommendationFacade {
  
  private final UserPostInteractionRepository postRepository;

  private static final Function<Object[], Post> toPost = objects -> (Post) objects[0];

  public Page<Post> findPosts(User currentUser, int page, int size) {
    return postRepository.findPosts(currentUser, PageRequest.of(page, size))
            .map(toPost);
  }

  public Page<Post> findPostsByCountry(User currentUser, Country country, int page, int size) {
    return postRepository.findPostsByCountry(currentUser, country, PageRequest.of(page, size))
            .map(toPost);
  }

  public Page<Post> findPostsByRelevance(@NotNull User currentUser, int page, int size) {
    List<Post> generalPostList = findPosts(currentUser, page, size).toList();

    //todo: implement user-interest scores then sort accordingly

    return new PageImpl<>(generalPostList, PageRequest.of(page, size), postRepository.countDistinct());
  }




  
}
