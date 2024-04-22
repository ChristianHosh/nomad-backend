package com.nomad.socialspring.recommender;

import com.nomad.socialspring.interest.InterestUserRepository;
import com.nomad.socialspring.interest.UserInterest;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostEventHandler {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserPostInteractionRepository userPostInteractionRepository;
  private final InterestUserRepository interestUserRepository;

  private List<UserInterest> getSharedInterests(@NotNull User user, @NotNull Post post) {
    Set<UserInterest> userInterests = new HashSet<>(interestUserRepository.findByUserAndInterestIn(user, post.getInterests()));
    userInterests.addAll(post.getInterests().stream()
            .map(interest -> UserInterest.of(interest, user))
            .toList());
    return saveAllInterests(userInterests);
  }

  private List<UserInterest> saveAllInterests(Collection<UserInterest> userInterests) {
    return interestUserRepository.saveAll(userInterests);
  }

  private void setNewInterestsStrength(List<UserInterest> interests, Event event, boolean increment) {
    if (increment)
      interests.forEach(interestUser -> interestUser.setScore(interestUser.getScore() + event.getStrength()));
    else
      interests.forEach(interestUser -> interestUser.setScore(interestUser.getScore() - event.getStrength()));
    saveAllInterests(interests);
  }

  private UserPostInteraction createPostInteraction(User user, Post post, Event event) {
    return new UserPostInteraction(user, event, post, event.getStrength());
  }

  private void deleteInteraction(UserPostInteraction interaction) {
    userPostInteractionRepository.delete(interaction);
  }

  private UserPostInteraction findInteraction(User user, Post post, Event event) {
    return userPostInteractionRepository.findByUserAndPostAndEventOrderByCreatedOnDesc(user, post, event).get(0);
  }

  @SuppressWarnings("unused")
  private void deleteInteractionBy(User user, Post post, Event event) {
    deleteInteraction(findInteraction(user, post, event));
  }

  public void viewPost(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.VIEW));
  }

  public void likePost(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.LIKE));
  }

  public void unlikePost(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.LIKE, false));
  }

  @SuppressWarnings("unused")
  public void favoritePost(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.FAVORITE));

  }

  @SuppressWarnings("unused")
  public void unfavoritePost(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.FAVORITE, false));
  }

  public void commentOnPost(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.COMMENT));
  }

  public void deleteComment(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.COMMENT, false));
  }

  public void likeComment(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.LIKE_COMMENT));
  }

  public void unlikeComment(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.LIKE_COMMENT, false));
  }

  public void joinTrip(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.JOIN));
  }

  public void leaveTrip(User user, Post post) {
    applicationEventPublisher.publishEvent(new UserPostEvent(user, post, Event.JOIN, false));

  }

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  @TransactionalEventListener(UserPostEvent.class)
  public void handleUserPostEvent(UserPostEvent userPostEvent) {
    log.info("starting to handle user post event: {}", userPostEvent);
    User user = userPostEvent.user();
    Post post = userPostEvent.post();
    Event event = userPostEvent.event();
    List<UserInterest> sharedInterests = getSharedInterests(user, post);
    setNewInterestsStrength(sharedInterests, event, userPostEvent.increment());

    UserPostInteraction postInteraction = createPostInteraction(user, post, event);
    postInteraction = userPostInteractionRepository.save(postInteraction);

    log.info("saved post interaction: {}", postInteraction);
  }
}
