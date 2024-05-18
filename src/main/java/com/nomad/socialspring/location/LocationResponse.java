package com.nomad.socialspring.location;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserResponse;
import lombok.Getter;

@Getter
public class LocationResponse extends BaseResponse {
  private final String name;
  private final String imageUrl;
  private final String about;
  private final UserResponse.CanReview canReview;

  public LocationResponse(Location location, User user) {
    super(location);
    name = location.getFullName();
    imageUrl = location.getImageUrl();
    about = location.getAbout();
    canReview = location.canBeReviewedBy(user);
  }

  public static LocationResponse fromEntity(Location location) {
    return location == null? null : new LocationResponse(location, null);
  }

  public static LocationResponse fromEntity(Location location, User user) {
    return location == null ? null : new LocationResponse(location, user);
  }
}
