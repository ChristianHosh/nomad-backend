package com.nomad.socialspring.recommender;

import com.nomad.socialspring.interest.InterestUserRepository;
import com.nomad.socialspring.interest.UserInterest;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostEventHandler {

  private final UserPostInteractionRepository userPostInteractionRepository;
  private final InterestUserRepository interestUserRepository;

  private List<UserInterest> getSharedInterests(@NotNull User user, @NotNull Post post) {
    Set<UserInterest> userInterests = new HashSet<>(interestUserRepository.findByUserAndInterestIn(user, post.getInterests()));
    userInterests.addAll(post.getInterests().stream()
            .map(interest -> UserInterest.of(interest, user))
            .toList());
    // may need to uncomment below
//    user.getInterests().addAll(userInterests);
    return saveAllInterests(userInterests);
  }

  private List<UserInterest> saveAllInterests(Collection<UserInterest> userInterests) {
    return interestUserRepository.saveAll(userInterests);
  }

  private List<UserInterest> setNewInterestsStrength(List<UserInterest> interests, Event event, boolean increment) {
    if (increment)
      interests.forEach(interestUser -> interestUser.setScore(interestUser.getScore() + event.getStrength()));
    else
      interests.forEach(interestUser -> interestUser.setScore(interestUser.getScore() - event.getStrength()));
    return saveAllInterests(interests);
  }

  private UserPostInteraction createPostInteraction(User user, Post post, Event event) {
    return new UserPostInteraction(user, event, post, event.getStrength());
  }

  private void deleteInteraction(UserPostInteraction interaction) {
    userPostInteractionRepository.delete(interaction);
  }

  private UserPostInteraction findInteraction(User user, Post post, Event event) {
    return userPostInteractionRepository.findByUserAndPostAndEventOrderByCreatedOnDesc(user, post, event).getFirst();
  }

  public void viewPost(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      Event event = Event.VIEW;
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, event, true);
    });
  }

  public void likePost(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      Event event = Event.LIKE;
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, event, true);

      UserPostInteraction postInteraction = createPostInteraction(user, post, event);
      postInteraction = userPostInteractionRepository.save(postInteraction);


    });
  }

  public void unlikePost(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.LIKE, false);

    });
  }

  public void favoritePost(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.FAVORITE, true);

    });
  }

  public void unfavoritePost(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.FAVORITE, false);

    });
  }

  public void commentOnPost(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.COMMENT, true);

    });
  }

  public void deleteComment(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.COMMENT, false);

    });
  }

  public void likeComment(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.LIKE_COMMENT, true);

    });
  }

  public void unlikeComment(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.LIKE_COMMENT, false);

    });
  }

  public void joinTrip(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.JOIN, true);

    });
  }

  public void leaveTrip(User user, Post post) {
    Thread.ofVirtual().start(() -> {
      List<UserInterest> sharedInterests = getSharedInterests(user, post);
      sharedInterests = setNewInterestsStrength(sharedInterests, Event.JOIN, false);

    });
  }
}
