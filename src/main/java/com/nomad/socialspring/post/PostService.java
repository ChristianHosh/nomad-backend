package com.nomad.socialspring.post;

import com.nomad.socialspring.chat.ChatChannel;
import com.nomad.socialspring.chat.ChatChannelFacade;
import com.nomad.socialspring.comment.Comment;
import com.nomad.socialspring.comment.CommentFacade;
import com.nomad.socialspring.comment.CommentRequest;
import com.nomad.socialspring.comment.CommentResponse;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.image.ImageFacade;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.InterestFacade;
import com.nomad.socialspring.interest.InterestResponse;
import com.nomad.socialspring.location.LocationFacade;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.recommender.PostEventHandler;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.trip.TripFacade;
import com.nomad.socialspring.trip.TripRequest;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import com.nomad.socialspring.user.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final UserFacade userFacade;
  private final PostFacade postFacade;
  private final ImageFacade imageFacade;
  private final InterestFacade interestFacade;
  private final CommentFacade commentFacade;
  private final NotificationFacade notificationFacade;
  private final LocationFacade locationFacade;
  private final TripFacade tripFacade;
  private final ChatChannelFacade chatChannelFacade;
  private final PostEventHandler postEventHandler;

  @Transactional
  public PostResponse createPost(@NotNull PostRequest request, List<MultipartFile> imageFiles) {
    User currentUser = userFacade.getCurrentUser();

    Set<Image> images = imageFacade.saveAll(imageFiles);
    Trip trip = null;
    if (request.locationId() != null) {
      TripRequest tripRequest = new TripRequest(
              Date.valueOf(request.startDate()),
              Date.valueOf(request.endDate()),
              request.locationId()
      );

      trip = tripFacade.save(tripRequest, locationFacade.findById(tripRequest.locationId()));

      ChatChannel chatChannel = chatChannelFacade.newChannel(trip.getLocation().getFullName() + " Trip", List.of(currentUser), currentUser);
      chatChannel.setTrip(trip);
      trip.setChatChannel(chatChannel);

      if (!trip.addParticipant(currentUser, true))
        throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_TRIP, currentUser);
    }

    if (request.interestsIds().isEmpty())
      request.interestsIds().add(21L); // GENERAL INTEREST

    Set<Interest> interestSet = interestFacade.getInterestsFromIds(request.interestsIds());

    Post post = postFacade.save(request, trip, currentUser, interestSet, images);
    return post.toResponse(currentUser);
  }

  @Transactional
  public PostResponse updatePost(Long postId, @NotNull PostRequest request) {
    User currentUser = userFacade.getCurrentUser();
    Post post = postFacade.findById(postId);

    // only author can update their posts
    if (post.canBeModifiedBy(currentUser)) {
      Set<Interest> interestSet = interestFacade.getInterestsFromIds(request.interestsIds());
      post.setContent(request.content());
      post.setIsPrivate(request.isPrivate());
      post.setInterests(interestSet);
      return postFacade.save(post).toResponse(currentUser);
    }
    throw BxException.unauthorized(currentUser);
  }

  public PostResponse getPost(Long postId) {
    User currentUser = userFacade.getCurrentUserOrNull();
    Post post = postFacade.findById(postId);

    // only return if post is public or current user follows post author
    if (post.canBeSeenBy(currentUser)) {
      postEventHandler.viewPost(currentUser, post);
      return post.toResponse(currentUser);
    }
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public PostResponse deletePost(Long postId) {
    User currentUser = userFacade.getCurrentUser();
    Post post = postFacade.findById(postId);

    if (post.canBeModifiedBy(currentUser)) {
      postFacade.delete(post);
      return post.toResponse();
    }
    throw BxException.unauthorized(currentUser);
  }

  public Page<CommentResponse> getPostComments(Long postId, int page, int size) {
    User currentUser = userFacade.getCurrentUserOrNull();
    Post post = postFacade.findById(postId);

    if (post.canBeSeenBy(currentUser))
      return commentFacade
              .findAllByPost(post, page, size)
              .map(c -> c.toResponse(currentUser));
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
    User currentUser = userFacade.getCurrentUser();
    Post post = postFacade.findById(postId);

    if (post.canBeSeenBy(currentUser)) {
      Comment comment = commentFacade.save(commentRequest, currentUser, post);
      post.getComments().add(comment);
      post = postFacade.save(post);
      if (!Objects.equals(currentUser, post.getAuthor()))
        notificationFacade.notifyPostComment(post, comment);

      notificationFacade.notifyMentions(userFacade.getMentionedUsersFromContent(comment.getContent()), comment);
      postEventHandler.commentOnPost(currentUser, post);
      return comment.toResponse(currentUser);
    }
    throw BxException.forbidden(currentUser);
  }

  public Page<UserResponse> getPostLikes(Long postId, int page, int size) {
    Post post = postFacade.findById(postId);
    User currentUser = userFacade.getCurrentUserOrNull();

    if (post.canBeSeenBy(currentUser))
      return userFacade
              .findAllByPostLiked(post, page, size)
              .map(u -> u.toResponse(currentUser));
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public PostResponse likePost(Long postId) {
    Post post = postFacade.findById(postId);
    User currentUser = userFacade.getCurrentUser();

    if (post.canBeSeenBy(currentUser)) {
      if (!post.getLikes().add(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_LIKE_POST, currentUser);

      if (!Objects.equals(currentUser, post.getAuthor()))
        notificationFacade.notifyPostLike(post, currentUser);

      log.info("calling like post from controller");
      postEventHandler.likePost(currentUser, post);
      log.info("finishing request handling");
      return postFacade.save(post).toResponse(currentUser);
    }
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public PostResponse unlikePost(Long postId) {
    Post post = postFacade.findById(postId);
    User currentUser = userFacade.getCurrentUser();

    if (post.canBeSeenBy(currentUser)) {
      if (!post.getLikes().remove(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_UNLIKE_POST, currentUser);

      postEventHandler.unlikePost(currentUser, post);
      return postFacade.save(post).toResponse(currentUser);
    }
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public PostResponse favoritePost(Long postId) {
    Post post = postFacade.findById(postId);
    User currentUser = userFacade.getCurrentUser();

    if (post.canBeSeenBy(currentUser)) {
      if (!post.getFavorites().add(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_FAVORITE_POST, currentUser);

      postEventHandler.favoritePost(currentUser, post);
      return postFacade.save(post).toResponse(currentUser);
    }
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public PostResponse unfavoritePost(Long postId) {
    Post post = postFacade.findById(postId);
    User currentUser = userFacade.getCurrentUser();

    if (post.canBeSeenBy(currentUser)) {
      if (!post.getFavorites().remove(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_UNFAVORITE_POST, currentUser);

      postEventHandler.unfavoritePost(currentUser, post);
      return postFacade.save(post).toResponse(currentUser);
    }
    throw BxException.forbidden(currentUser);
  }


  public Page<PostResponse> getFavoritePosts(int page, int size) {
    User user = userFacade.getCurrentUser();
    return postFacade.findAllByFavorites(user, page, size).map(p -> p.toResponse(user));
  }

  public Page<InterestResponse> getPostInterests(Long postId) {
    Post post = postFacade.findById(postId);

    var interests = post.getInterests();
    return new PageImpl<>(
            interests.stream().map(InterestResponse::fromEntity).toList(),
            PageRequest.of(0, interests.size()),
            interests.size()
    );
  }
}
