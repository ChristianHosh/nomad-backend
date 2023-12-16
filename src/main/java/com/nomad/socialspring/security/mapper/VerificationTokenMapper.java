package com.nomad.socialspring.security.mapper;

import com.nomad.socialspring.security.model.VerificationToken;
import com.nomad.socialspring.security.model.VerificationType;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.util.BDate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VerificationTokenMapper {

    @NotNull
    @Contract("_-> new")
    public static VerificationToken accountToken(@NotNull User user) {
        return VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .expirationDate(BDate.currentDate().addDay(1))
                .verificationType(VerificationType.ACCOUNT)
                .user(user)
                .build();
    }
}
