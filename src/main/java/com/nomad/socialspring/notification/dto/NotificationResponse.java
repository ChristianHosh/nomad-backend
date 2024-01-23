package com.nomad.socialspring.notification.dto;

import com.nomad.socialspring.notification.model.NotificationType;
import com.nomad.socialspring.user.dto.UserResponse;
import lombok.Builder;

import java.sql.Timestamp;

/**
 * Response DTO for {@link com.nomad.socialspring.notification.model.Notification}
 */
@Builder
public record NotificationResponse(
        Long id,
        Timestamp createdOn,
        Timestamp updatedOn,
        NotificationType notificationType,
        String content,
        Long entityId,
        UserResponse author
) {
}