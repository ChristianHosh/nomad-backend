package com.nomad.socialspring.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ChatChannelUsersRequest(

    @NotNull(message = "ids is null")
    @NotEmpty(message = "ids is empty")
    List<@NotNull(message = "ids is null") Long> userIds

) {
}
