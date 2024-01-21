package com.nomad.socialspring.user.model;

import com.nomad.socialspring.user.dto.FollowRequestResponse;
import org.jetbrains.annotations.NotNull;

public class FollowRequestMapper {

    public static FollowRequestResponse entityToResponse(@NotNull FollowRequest followRequest) {
        return FollowRequestResponse.builder()
                .id(followRequest.getId())
                .fromUser(UserMapper.entityToResponse(followRequest.getFromUser(), followRequest.getToUser()))
                .build();
    }
}
