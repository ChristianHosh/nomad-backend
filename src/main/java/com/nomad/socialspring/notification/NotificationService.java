package com.nomad.socialspring.notification;

import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationFacade notificationFacade;
  private final UserFacade userFacade;

  @Transactional
  public Page<NotificationResponse> getNotifications(int page, int size) {
    User currentUser = userFacade.getCurrentUser();

    return notificationFacade
            .getNotifications(currentUser, page, size)
            .map(Notification::toResponse);
  }
}
