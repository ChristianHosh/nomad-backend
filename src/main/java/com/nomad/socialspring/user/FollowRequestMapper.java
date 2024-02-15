package com.nomad.socialspring.user;

import org.jetbrains.annotations.NotNull;

public class FollowRequestMapper {

  public static FollowRequestResponse entityToResponse(@NotNull FollowRequest followRequest) {
    return FollowRequestResponse.builder()
            .id(followRequest.getId())
            .fromUser(UserMapper.entityToResponse(followRequest.getFromUser(), followRequest.getToUser()))
            .build();
  }
}
