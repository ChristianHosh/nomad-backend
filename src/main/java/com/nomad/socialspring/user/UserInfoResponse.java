package com.nomad.socialspring.user;

public record UserInfoResponse(

    long unreadMessagesCount,
    long unreadNotificationsCount
) {
}
