package com.nomad.socialspring.post;

import com.nomad.socialspring.chat.ChatChannel;
import com.nomad.socialspring.chat.ChatChannelFacade;
import com.nomad.socialspring.comment.*;
import com.nomad.socialspring.location.LocationFacade;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.image.ImageFacade;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.InterestFacade;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.recommender.PostEventHandler;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.trip.TripFacade;
import com.nomad.socialspring.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    if (request.trip() != null) {
      trip = tripFacade.save(request.trip(), locationFacade.findById(request.trip().countryId()));

      ChatChannel chatChannel = ChatChannel.builder()
              .trip(trip)
              .build();
      if (!chatChannel.addUser(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_CHANNEL, currentUser);
      chatChannelFacade.save(chatChannel);

      if (!trip.addParticipant(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_TRIP, currentUser);
    }

    Set<Interest> interestSet = interestFacade.getInterestFromTags(request.interestsTags());

    Post post = postFacade.save(request, trip, currentUser, interestSet, images);
    return post.toResponse(currentUser);
  }

  @Transactional
  public PostResponse updatePost(Long postId, @NotNull PostRequest request) {
    User currentUser = userFacade.getCurrentUser();
    Post post = postFacade.findById(postId);

    Set<Interest> interestSet = interestFacade.getInterestFromTags(request.interestsTags());
    // only author can update their posts
    if (post.canBeModifiedBy(currentUser)) {
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
    throw BxException.unauthorized(currentUser);
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
    throw BxException.unauthorized(currentUser);
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
    throw BxException.unauthorized(currentUser);
  }

  public Page<UserResponse> getPostLikes(Long postId, int page, int size) {
    Post post = postFacade.findById(postId);
    User currentUser = userFacade.getCurrentUserOrNull();

    if (post.canBeSeenBy(currentUser))
      return userFacade
              .findAllByPostLiked(post, page, size)
              .map(u -> u.toResponse(currentUser));
    throw BxException.unauthorized(currentUser);
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

      postEventHandler.likePost(currentUser, post);
      return postFacade.save(post).toResponse(currentUser);
    }
    throw BxException.unauthorized(currentUser);
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
    throw BxException.unauthorized(currentUser);
  }

}
