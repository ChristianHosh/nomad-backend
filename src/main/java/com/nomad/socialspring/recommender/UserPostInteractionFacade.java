package com.nomad.socialspring.recommender;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPostInteractionFacade {
  
  private final UserPostInteractionRepository repository;
  
  Page<Post> findPostsByScore(User currentUser, int page, int size) {
    return repository.findPostsByScore(currentUser, PageRequest.of(page, size))
        .map(objects -> (Post) objects[0]);
  }
  
  double getScore(List<UserPostInteraction> events) {
    return events.stream()
        .mapToInt(UserPostInteraction::getStrength)
        .sum();
  }
}
