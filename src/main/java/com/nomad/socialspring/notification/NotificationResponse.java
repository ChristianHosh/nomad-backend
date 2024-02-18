package com.nomad.socialspring.notification;

import com.nomad.socialspring.user.UserResponseR;
import lombok.Builder;

import java.sql.Timestamp;

/**
 * Response DTO for {@link Notification}
 */
@Builder
public record NotificationResponse(
        Long id,
        Timestamp createdOn,
        Timestamp updatedOn,
        NotificationType notificationType,
        String content,
        Long entityId,
        UserResponseR author
) {
}