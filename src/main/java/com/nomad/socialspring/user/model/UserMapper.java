package com.nomad.socialspring.user.model;

import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.facade.AuthenticationFacade;
import com.nomad.socialspring.user.dto.FollowStatus;
import com.nomad.socialspring.user.dto.UserResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    private final static BCryptPasswordEncoder encoder = AuthenticationFacade.getEncoder();

    @NotNull
    @Contract("_ -> new")
    public static User requestToEntity(@NotNull RegisterRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(encoder.encode(request.password()))
                .email(request.email())
                .role(Role.USER)
                .isVerified(false)
                .build();
        user.setProfile(Profile.builder()
                .user(user)
                .displayName(request.displayName())
                .build());
        return user;
    }

    public static UserResponse entityToResponse(User user) {
        return entityToResponse(user, null, null, false);
    }

    public static UserResponse entityToResponse(User user, User currentUser) {
        return entityToResponse(user, null, currentUser, false);
    }

    public static UserResponse entityToResponse(User user, String token) {
        return entityToResponse(user, token, null, false);
    }

    public static UserResponse entityToResponse(User user, User currentUser, boolean detailedProfile) {
        return entityToResponse(user, null, currentUser, detailedProfile);
    }

    public static UserResponse entityToResponse(User user, String token, User currentUser, boolean detailedProfile) {
        if (user == null)
            return null;

        FollowStatus followStatus = getFollowStatus(user, currentUser);

        return UserResponse.builder()
                .id(user.getId())
                .createdOn(user.getCreatedOn())
                .updatedOn(user.getUpdatedOn())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .followStatus(followStatus)
                .rating(detailedProfile ? user.getRating() : null)
                .profile(ProfileMapper.entityToRequest(user.getProfile(), detailedProfile))
                .token(token)
                .build();
    }

    private static FollowStatus getFollowStatus(User user, User currentUser) {
        FollowStatus followStatus = FollowStatus.UNKNOWN;
        if (currentUser != null) {
            if (currentUser.follows(user))
                followStatus = FollowStatus.FOLLOWING;
            else
                if (user.hasPendingRequestFrom(currentUser))
                    followStatus = FollowStatus.PENDING;
                else
                    followStatus = FollowStatus.CAN_FOLLOW;
        }
        return followStatus;
    }

}
