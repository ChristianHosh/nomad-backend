package com.nomad.socialspring.notification.service;

import com.nomad.socialspring.notification.dto.NotificationResponse;
import com.nomad.socialspring.notification.model.NotificationMapper;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.service.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationFacade notificationFacade;
    private final UserFacade userFacade;

    public Page<NotificationResponse> getNotifications(int page, int size) {
        User currentUser = userFacade.getCurrentUser();

        return notificationFacade
                .getNotifications(currentUser, page, size)
                .map(NotificationMapper::entityToResponse);
    }
}
