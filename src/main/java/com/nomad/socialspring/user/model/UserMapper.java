package com.nomad.socialspring.user.model;

import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.facade.AuthenticationFacade;
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
        return entityToResponse(user, null, null);
    }

    public static UserResponse entityToResponse(User user, User currentUser) {
        return entityToResponse(user, null, currentUser);
    }

    public static UserResponse entityToResponse(User user, String token) {
        return entityToResponse(user, token, null);
    }

    public static UserResponse entityToResponse(User user, String token, User currentUser) {
        if (user == null)
            return null;

        boolean canFollow = true;
        if (currentUser == null)
            canFollow = false;
        else if (currentUser.follows(user))
            canFollow = false;
        else if (user.hasPendingRequestFrom(currentUser))
            canFollow = false;

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .canFollow(canFollow)
                .profile(ProfileMapper.entityToRequest(user.getProfile()))
                .token(token)
                .build();
    }

}
