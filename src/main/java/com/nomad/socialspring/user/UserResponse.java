package com.nomad.socialspring.user;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.interest.InterestResponse;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class UserResponse extends BaseResponse {

  private final String username;
  private final String email;
  private final Role role;
  private final FollowStatus followStatus;
  private final ProfileResponse profile;
  private final CanReview canReview;
  private final Boolean canBlock;
  private List<InterestResponse> interests;
  private Integer rating;
  private String token;

  private UserResponse(User entity, User user, boolean detailed) {
    super(entity);
    this.followStatus = followStat(entity, user);

    username = entity.getUsername();
    email = entity.getEmail();
    role = entity.getRole();

    canReview = detailed ? CanReview.DISABLED : entity.canBeReviewedBy(user);
    canBlock = detailed && entity.isNotBlockedBy(user);

    if (detailed) {
      rating = entity.getRating();
      interests = entity.getInterestsSorted().stream()
          .map(InterestResponse::fromEntity)
          .toList();
    }

    profile = ProfileResponse.fromEntity(entity.getProfile(), detailed);
  }

  public static UserResponse fromEntity(User user) {
    return fromEntity(user, null);
  }

  public static UserResponse fromEntity(User user, User currentUser) {
    return fromEntity(user, currentUser, false);
  }

  public static UserResponse fromEntity(User user, User currentUser, boolean detailed) {
    return user == null ? null : new UserResponse(user, currentUser, detailed);
  }

  private static FollowStatus followStat(User user, User currentUser) {
    FollowStatus followStatus = FollowStatus.UNKNOWN;
    if (currentUser != null) {
      Set<User> followings = currentUser.getFollowings();
      if (followings.contains(user))
        followStatus = FollowStatus.FOLLOWING;
      else if (user.hasPendingRequestFrom(currentUser))
        followStatus = FollowStatus.PENDING;
      else
        followStatus = FollowStatus.CAN_FOLLOW;
    }
    return followStatus;
  }

  public UserResponse withToken(String token) {
    this.token = token;
    return this;
  }

  public enum CanReview {
    DISABLED,
    REVIEWED,
    CAN_REVIEW
  }
}
