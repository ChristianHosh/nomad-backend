package com.nomad.socialspring.notification.model;


import com.nomad.socialspring.notification.dto.NotificationResponse;
import com.nomad.socialspring.user.model.UserMapper;
import org.jetbrains.annotations.NotNull;

public class NotificationMapper {

    public static NotificationResponse entityToResponse(@NotNull Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .createdOn(notification.getCreatedOn())
                .updatedOn(notification.getUpdatedOn())
                .content(notification.getContent())
                .author(UserMapper.entityToResponse(notification.getAuthor(), notification.getRecipient()))
                .notificationType(notification.getNotificationType())
                .entityId(notification.getEntityId())
                .build();
    }
}
