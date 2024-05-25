package com.nomad.socialspring.recommender;

import com.nomad.socialspring.interest.UserInterest;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.post.PostRepository;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostRecommendationFacade {

  private final PostRepository postRepository;

  public static List<Post> sortByInterests(List<Post> content, User user) {
    List<UserInterest> userInterestList = new ArrayList<>(user.getInterests());

    List<Pair<Post, Double>> rScores = content.stream()
        .map(post -> {
          double score = 0.0;
          for (UserInterest userInterest : userInterestList) {
            if (post.getInterests().contains(userInterest.getInterest())) {
              score += userInterest.getScore();
            }
          }
          return Pair.of(post, score);
        })
        .toList();

    DoubleSummaryStatistics rScoreStats = rScores.stream()
        .mapToDouble(Pair::getSecond)
        .summaryStatistics();

    long count = rScoreStats.getCount();
    double mean = rScoreStats.getAverage();

    double standardDeviation = 0.0;
    for (Pair<Post, Double> pair : rScores) {
      standardDeviation += Math.pow(pair.getSecond() - mean, 2);
    }
    standardDeviation = Math.sqrt(standardDeviation / count);
    double finalStandardDeviation = standardDeviation;

    List<Pair<Post, Double>> zScores = rScores.stream()
        .map(pair -> Pair.of(pair.getFirst(), (pair.getSecond() - mean) / finalStandardDeviation))
        .toList();

    return zScores.stream()
        .sorted(Comparator.comparingDouble(Pair::getSecond))
        .map(Pair::getFirst)
        .toList();
  }

  public Page<PostResponse> findPosts(User currentUser, int page, int size) {
    Page<Post> postPage = postRepository.findPosts(currentUser, PageRequest.of(page, size));

    if (currentUser == null)
      return postPage.map(Post::toResponse);
    List<Post> postList = sortByInterests(postPage.getContent(), currentUser);

    return new PageImpl<>(postList, PageRequest.of(page, size), postPage.getTotalElements())
        .map(p -> p.toResponse(currentUser));
  }

  public Page<PostResponse> findFollowingsRecommendations(User currentUser, int page, int size) {
    Page<Post> postPage = postRepository.findFollowingsPosts(currentUser, PageRequest.of(page, size));

    List<Post> postList = sortByInterests(postPage.getContent(), currentUser);

    return new PageImpl<>(postList, PageRequest.of(page, size), postPage.getTotalElements())
        .map(p -> p.toResponse(currentUser));
  }

  public Page<PostResponse> findLocalTripsRecommendations(User currentUser, int page, int size) {
    Page<Post> postPage = postRepository.findLocalTrips(currentUser,
        currentUser.getProfile().getLocation(),
        PageRequest.of(page, size));

    List<Post> postList = sortByInterests(postPage.getContent(), currentUser);

    return new PageImpl<>(postList, PageRequest.of(page, size), postPage.getTotalElements())
        .map(p -> p.toResponse(currentUser));
  }
}
