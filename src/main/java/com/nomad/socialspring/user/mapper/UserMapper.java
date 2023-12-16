package com.nomad.socialspring.user.mapper;

import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.facade.AuthenticationFacade;
import com.nomad.socialspring.user.model.Profile;
import com.nomad.socialspring.user.model.Role;
import com.nomad.socialspring.user.model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    private final static BCryptPasswordEncoder encoder = AuthenticationFacade.getEncoder();

    @NotNull
    @Contract("_-> new")
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
}
