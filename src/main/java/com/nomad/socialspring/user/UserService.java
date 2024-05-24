package com.nomad.socialspring.user;

import com.nomad.socialspring.chat.ChatChannelUserFacade;
import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.location.LocationFacade;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.ImageFacade;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.InterestFacade;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.post.PostFacade;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.review.Review;
import com.nomad.socialspring.review.ReviewFacade;
import com.nomad.socialspring.review.ReviewRequest;
import com.nomad.socialspring.review.ReviewResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserFacade userFacade;
  private final FollowRequestFacade followRequestFacade;
  private final InterestFacade interestFacade;
  private final ImageFacade imageFacade;
  private final LocationFacade locationFacade;
  private final NotificationFacade notificationFacade;
  private final ReviewFacade reviewFacade;
  private final ChatChannelUserFacade chatChannelUserFacade;
  private final PostFacade postFacade;

  public UserResponse getUser(Long userId) {
    User currentUser = userFacade.getCurrentUserOrNull();
    User user = userFacade.findById(userId);

    if (currentUser.isNotBlockedBy(user))
      return user.toResponse(currentUser, true);
    throw BxException.forbidden(currentUser);
  }

  public UserResponse getUser(String username) {
    User currentUser = userFacade.getCurrentUserOrNull();
    User user = userFacade.findByUsername(username);

    if (currentUser.isNotBlockedBy(user))
      return user.toResponse(currentUser, true);
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public UserResponse followUser(Long userId) {
    User user = userFacade.findById(userId);
    User currentUser = userFacade.getCurrentUser();

    if (user.canBeSeenBy(currentUser)) {
      if (currentUser.follows(user))
        throw BxException.badRequest(User.class, BxException.X_CURRENT_USER_ALREADY_FOLLOWS);

      FollowRequest followRequest = followRequestFacade.save(new FollowRequest(currentUser, user));
      notificationFacade.notifyFollowRequest(followRequest);

      return user.toResponse(currentUser, true);
    }
    throw BxException.forbidden(currentUser);
  }

  @Transactional
  public UserResponse unfollowUser(Long userId) {
    User user = userFacade.findById(userId);
    User currentUser = userFacade.getCurrentUser();

    if (user.canBeSeenBy(currentUser)) {
      if (!currentUser.follows(user))
        throw BxException.badRequest(User.class, BxException.X_CURRENT_USER_ALREADY_UNFOLLOWS);

      if (currentUser.removeFollowing(user))
        return userFacade.save(user).toResponse(currentUser, true);
      throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_FOLLOWER, currentUser);
    }
    throw BxException.forbidden(currentUser);
  }


  public Page<FollowRequestResponse> getUserFollowRequests(int page, int size) {
    User currentUser = userFacade.getCurrentUser();

    return followRequestFacade.findByUser(currentUser, page, size).map(FollowRequestResponse::fromEntity);
  }

  @Transactional
  public UserResponse acceptFollowRequest(Long followRequestId) {
    FollowRequest followRequest = followRequestFacade.findById(followRequestId);
    User currentUser = userFacade.getCurrentUser();

    if (followRequest.isForUser(currentUser)) {
      if (!followRequest.getFromUser().addFollowing(currentUser))
        throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_FOLLOWER, currentUser);
      deleteFollowRequestAndNotification(followRequest);
    }

    return userFacade.save(followRequest.getFromUser()).toResponse();
  }

  @Transactional
  public UserResponse declineFollowRequest(Long followRequestId) {
    FollowRequest followRequest = followRequestFacade.findById(followRequestId);
    User currentUser = userFacade.getCurrentUser();

    if (followRequest.isForUser(currentUser)) {
      deleteFollowRequestAndNotification(followRequest);
    }

    return userFacade.save(followRequest.getFromUser()).toResponse();
  }

  private void deleteFollowRequestAndNotification(FollowRequest followRequest) {
    followRequestFacade.delete(followRequest);
    notificationFacade.deleteFollowNotification(followRequest);
  }

  public Page<UserResponse> getUserFollowers(Long userId, int page, int size) {
    User user = userFacade.findById(userId);
    User currentUser = userFacade.getCurrentUserOrNull();

    return userFacade
            .getFollowersByUser(user.getId(), page, size)
            .map(u -> u.toResponse(currentUser));
  }

  public Page<UserResponse> getUserFollowings(Long userId, int page, int size) {
    User user = userFacade.findById(userId);
    User currentUser = userFacade.getCurrentUserOrNull();

    return userFacade
            .getFollowingsByUser(user.getId(), page, size)
            .map(u -> u.toResponse(currentUser));
  }

  public UserResponse updateProfileInfo(@NotNull ProfileRequest request) {
    User currentUser = userFacade.getCurrentUser();

    Set<Interest> interestSet = interestFacade.getInterestsFromIds(request.interestsIds());

    Location location = null;
    if (request.locationId() != null)
      location = locationFacade.findById(request.locationId());

    currentUser = userFacade.updateProfile(currentUser, request, interestSet, location);

    return currentUser.toResponse(null, true);
  }

  public UserResponse updateProfileImage(MultipartFile imageFile) {
    User currentUser = userFacade.getCurrentUser();

    currentUser.getProfile().setProfileImage(imageFacade.save(imageFile));

    return userFacade.save(currentUser).toResponse();
  }

  public UserResponse deleteProfileImage() {
    User currentUser = userFacade.getCurrentUser();

    currentUser.getProfile().setProfileImage(null);

    return userFacade.save(currentUser).toResponse();
  }

  public Page<UserResponse> getUserMutualFollowings(Long userId, int page, int size) {
    User user = userFacade.findById(userId);
    User currentUser = userFacade.getCurrentUser();

    return userFacade
            .getMutualFollowings(user, currentUser, page, size)
            .map(u -> u.toResponse(currentUser));
  }

  public List<UserResponse> getSuggestedUsers() {
    User currentUser = userFacade.getCurrentUser();

    return userFacade
            .getSuggestedUsers(currentUser)
            .stream()
            .map(u -> u.toResponse(currentUser))
            .toList();
  }

  public UserResponse createUserReview(Long userId, ReviewRequest reviewRequest) {
    User currentUser = userFacade.getCurrentUser();
    User user = userFacade.findById(userId);

    if (user.isReviewedBy(currentUser))
      throw BxException.badRequest(User.class, BxException.X_USER_ALREADY_REVIEWED);

    Review review = reviewFacade.save(reviewRequest, currentUser, user);
    notificationFacade.notifyUserReview(review);

    return user.toResponse(currentUser, true);
  }

  public UserResponse blockUser(Long userId) {
    User currentUser = userFacade.getCurrentUser();
    User user = userFacade.findById(userId);

    if (user.follows(currentUser) && (!user.removeFollowing(currentUser)))
        throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_FOLLOWER, currentUser);
    if (currentUser.follows(user) && (!currentUser.removeFollowing(user)))
        throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_FOLLOWER, user);
    if (user.hasPendingRequestFrom(currentUser) && (!user.removeFollowRequestFrom(currentUser)))
        throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_FOLLOW_REQUEST, currentUser);
    if (currentUser.hasPendingRequestFrom(user) && (!currentUser.removeFollowRequestFrom(user)))
        throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_FOLLOW_REQUEST, user);
    if (currentUser.getBlockedUsers().add(user)) {
      userFacade.save(currentUser);
      return user.toResponse(currentUser);
    }
    throw BxException.hardcoded(BxException.X_COULD_NOT_BLOCK_USER, user);
  }

  public UserResponse unblockUser(Long userId) {
    User currentUser = userFacade.getCurrentUser();
    User user = userFacade.findById(userId);

    if (currentUser.getBlockedUsers().remove(user)) {
      userFacade.save(currentUser);
      return user.toResponse(currentUser);
    }
    throw BxException.hardcoded(BxException.X_COULD_NOT_UNBLOCK_USER, user);
  }

  public Page<UserResponse> getBlockedUsers(int page, int size) {
    User currentUser = userFacade.getCurrentUser();

    return userFacade
            .getBlockedUsers(currentUser, page, size)
            .map(User::toResponse);
  }

  public Page<UserResponse> getAllUsers(String query, boolean excludeSelf, int page, int size) {
    User currentUser = userFacade.getCurrentUserOrNull();

    return userFacade
            .findBySearchParamExcludeBlocked(currentUser, query, excludeSelf, page, size)
            .map(u -> u.toResponse(currentUser));
  }

  public UserInfoResponse getUserInfo() {
    User user = userFacade.getCurrentUser();

    long unreadMessagesCount = chatChannelUserFacade.countUnreadMessages(user);
    long unreadNotificationsCount = notificationFacade.countUnreadNotifications(user);


    return new UserInfoResponse(unreadMessagesCount, unreadNotificationsCount);
  }

  public Page<PostResponse> getUserPosts(Long userId, int page, int size) {
    User currentUser = userFacade.getCurrentUserOrNull();
    User user = userFacade.findById(userId);

    return postFacade.findByUser(user, currentUser, page, size)
            .map(p -> p.toResponse(currentUser));
  }

  public Page<ReviewResponse> getUserReviews(Long userId, int page, int size) {
    return reviewFacade.findByUser(userFacade.findById(userId), page, size)
            .map(Review::toResponse);
  }
}
