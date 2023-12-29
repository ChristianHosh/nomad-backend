package com.nomad.socialspring.notification.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_NOTIFICATION")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Enumerated
    @Column(name = "NOTIFICATION_TYPE", nullable = false)
    private NotificationType notificationType;

    @Column(name = "CONTENT", nullable = false)
    @Size(max = 255)
    private String content;

    @Column(name = "IS_READ", nullable = false)
    private Boolean isRead = false;

    @Column(name = "ENTITY_ID", nullable = false)
    private Long entityId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "RECIPIENT_ID")
    private User recipient;

    @Override
    public String getExceptionString() {
        return getId().toString();
    }
}