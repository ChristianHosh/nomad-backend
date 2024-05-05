package com.nomad.socialspring.user;

import com.nomad.socialspring.security.AuthenticationFacade;
import com.nomad.socialspring.security.RegisterRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

  private static final BCryptPasswordEncoder encoder = AuthenticationFacade.getEncoder();

  private UserMapper() {}

  @NotNull
  @Contract("_ -> new")
  public static User requestToEntity(@NotNull RegisterRequest request) {
    User user = User.builder()
            .username(request.username())
            .password(encoder.encode(request.password()))
            .email(request.email())
            .role(Role.ROLE_USER)
            .isVerified(false)
            .build();
    user.setProfile(Profile.builder()
            .user(user)
            .displayName(request.displayName())
            .build());
    return user;
  }

}
