package com.nomad.socialspring.security;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.user.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VerificationTokenMapper {

  private VerificationTokenMapper() {
  }

  @NotNull
  @Contract("_ -> new")
  public static VerificationToken accountToken(@NotNull User user) {
    return VerificationToken.builder()
        .token(UUID.randomUUID().toString())
        .expirationDate(BDate.currentDate().addDay(1))
        .verificationType(VerificationType.ACCOUNT)
        .user(user)
        .build();
  }
}
