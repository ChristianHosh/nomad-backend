package com.nomad.socialspring.notification.repo;

import com.nomad.socialspring.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}