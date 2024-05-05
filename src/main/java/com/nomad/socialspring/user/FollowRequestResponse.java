package com.nomad.socialspring.user;

import lombok.Builder;

/**
 * Response DTO for {@link FollowRequest}
 */
@Builder
public record FollowRequestResponse(Long id, UserResponse fromUser) {

  public static FollowRequestResponse fromEntity(FollowRequest followRequest) {
    return FollowRequestResponse.builder()
       .id(followRequest.getId())
       .fromUser(UserResponse.fromEntity(followRequest.getFromUser()))
       .build();
  }
}