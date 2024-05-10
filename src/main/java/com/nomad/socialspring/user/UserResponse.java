package com.nomad.socialspring.user;

import com.nomad.socialspring.common.BaseResponse;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserResponse extends BaseResponse {

  public enum CanReview {
    DISABLED,
    REVIEWED,
    CAN_REVIEW
  }

  private final String username;
  private final String email;
  private final Role role;
  private final FollowStatus followStatus;
  private final ProfileResponse profile;
  private final CanReview canReview;
  private final Boolean canBlock;
  private Integer rating;
  private String token;


  private UserResponse(User user, User currentUser, boolean detailed) {
    super(user);
    this.followStatus = followStat(user, currentUser);

    username = user.getUsername();
    email = user.getEmail();
    role = user.getRole();

    canReview = detailed ? CanReview.DISABLED : user.canBeReviewedBy(currentUser);
    canBlock = detailed && user.isNotBlockedBy(currentUser);
    
    if (detailed)
      rating = user.getRating();
    profile = ProfileResponse.fromEntity(user.getProfile(), detailed);
  }
  
  public UserResponse withToken(String token) {
    this.token = token;
    return this;
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
}
