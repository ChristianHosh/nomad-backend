package com.nomad.socialspring.notification;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.user.UserResponse;
import lombok.Getter;

@Getter
public class NotificationResponse extends BaseResponse {

  private final String content;
  private final UserResponse author;
  private final NotificationType type;
  private final Long entityId;
  private final Boolean isRead;

  private NotificationResponse(Notification notification) {
    super(notification);

    content = notification.getContent();
    author = notification.getAuthor().toResponse();
    type = notification.getNotificationType();
    entityId = notification.getEntityId();
    isRead = notification.getIsRead();
  }

  public static NotificationResponse fromEntity(Notification notification) {
    return notification == null ? null : new NotificationResponse(notification);
  }
}
