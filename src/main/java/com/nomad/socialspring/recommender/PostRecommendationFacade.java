package com.nomad.socialspring.recommender;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.country.Country;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.UserInterest;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PostRecommendationFacade {

  private final UserPostInteractionRepository postRepository;

  public Page<Post> findPosts(User currentUser, int page, int size) {
    return findPostsByTrendingAfter(currentUser, BDate.currentDate().addMonth(-1), page, size);
  }

  public Page<Post> findPostsByCountry(User currentUser, Country country, int page, int size) {
    return postRepository.findPostsByCountry(currentUser, country, PageRequest.of(page, size));
  }

  public Page<Post> findPostsByTrendingAfter(User currentUser, BDate date, int page, int size) {
    return postRepository.findPostsByTrendingAfter(currentUser, date, PageRequest.of(page, size));
  }

  public Page<Post> findPostsByRelevance(@NotNull User currentUser, int page, int size) {
    List<Post> generalPostList = findPosts(currentUser, page, size).toList();

    Set<UserInterest> userInterests = currentUser.getInterests();
    generalPostList.sort((p1, p2) -> {
      Set<Interest> p1Interests = p1.getInterests();
      Set<Interest> p2Interests = p2.getInterests();

      AtomicReference<Double> p1Score = new AtomicReference<>((double) 0);
      double p2Score = 0;
      Thread p1ScorerThread = Thread.ofVirtual().start(() -> {
        for (Interest interest : p1Interests) {
          for (UserInterest userInterest : userInterests) {
            if (Objects.equals(userInterest.getInterest(), interest)) {
              if (userInterest.getIsSetFromProfile())
                p1Score.updateAndGet(oldValue -> oldValue + userInterest.getScore() * 2);
              else
                p1Score.updateAndGet(oldValue -> oldValue + userInterest.getScore());
            }
          }
        }
      });
      for (Interest interest : p2Interests) {
        for (UserInterest userInterest : userInterests) {
          if (Objects.equals(userInterest.getInterest(), interest)) {
            if (userInterest.getIsSetFromProfile())
              p2Score += userInterest.getScore() * 2;
            else
              p2Score += userInterest.getScore();
          }
        }
      }

      try {
        p1ScorerThread.join();
      } catch (InterruptedException e) {
        throw BxException.unexpected(e);
      }

      return Double.compare(p1Score.get(), p2Score);
    });

    return new PageImpl<>(generalPostList, PageRequest.of(page, size), postRepository.countDistinct());
  }

}
