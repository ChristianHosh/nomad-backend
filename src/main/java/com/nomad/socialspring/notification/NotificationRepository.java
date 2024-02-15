package com.nomad.socialspring.notification;

import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.recipient = :recipient")
    Page<Notification> findByRecipient(@Param("recipient") User recipient, Pageable pageable);

    @Modifying
    @Query("delete from Notification n where n.entityId = :entityId and n.notificationType = com.nomad.socialspring.notification.NotificationType.FOLLOW")
    void deleteByFollowRequestId(@Param("entityId") Long entityId);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.id in :id and n.isRead = false")
    void updateIsReadByIdInAndIsReadFalse(@Param("ids") Collection<Long> ids);
}