package com.nomad.socialspring.recommender;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class GScoreScheduler {

  private final PostRepository postRepository;
  private final UserPostInteractionRepository postInteractionRepository;

  @Scheduled(initialDelay = 1, fixedDelay = 3, timeUnit = TimeUnit.HOURS)
  public void computeGScores() {
    log.info("STARTING G-SCORE CALCULATIONS");
    List<Object[]> postGScoreList = postInteractionRepository.findPostsWithInteractionsAfter(BDate.currentDate().addDay(-30));
    log.info("FOUND [%d] POSTS TO UPDATE".formatted(postGScoreList.size()));

    List<Pair<Post, Double>> postScoreList = postGScoreList.stream()
        .map(objects -> Pair.of((Post) objects[0], (Long) objects[1]))
        .map(postLongPair -> Pair.of(postLongPair.getFirst(), postLongPair.getSecond() + postLongPair.getFirst().getRecencyScore()))
        .toList();
    
    List<Double> gScores = postScoreList.stream()
        .mapToDouble(Pair::getSecond)
        .boxed().toList();
    
    DoubleSummaryStatistics gScoreStats = gScores.stream()
        .mapToDouble(Double::doubleValue)
        .summaryStatistics();

    long count = gScoreStats.getCount();
    double mean = gScoreStats.getAverage();
    log.info("G-SCORE MEAN = [%f]".formatted(mean));

    double standardDeviation = 0.0;
    for (Double gScore : gScores) {
      standardDeviation += Math.pow(gScore - mean, 2);
    }
    standardDeviation = Math.sqrt(standardDeviation / count);
    log.info("G-SCORE SD = [%f]".formatted(standardDeviation));

    double finalStandardDeviation = standardDeviation;
    postScoreList.forEach(postDoublePair -> {
      Post post = postDoublePair.getFirst();
      double gScore = postDoublePair.getSecond();
      double zScore = (gScore - mean) / finalStandardDeviation;
      post.setZScore(zScore);
    });
    postRepository.saveAll(postScoreList.stream().map(Pair::getFirst).toList());
    log.info("UPDATED GZ-SCORES FOR [%d] POSTS".formatted(count));
  }

}
