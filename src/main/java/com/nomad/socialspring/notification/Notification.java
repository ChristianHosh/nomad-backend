package com.nomad.socialspring.notification;

import com.nomad.socialspring.chat.ChatChannel;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.review.Review;
import com.nomad.socialspring.user.FollowRequest;
import com.nomad.socialspring.user.User;
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

  @Enumerated
  @Column(name = "NOTIFICATION_TYPE", nullable = false)
  private NotificationType notificationType;

  @Column(name = "CONTENT", nullable = false)
  @Size(max = 255)
  private String content;

  @Column(name = "IS_READ", nullable = false)
  @Builder.Default
  private Boolean isRead = false;

  @Column(name = "ENTITY_ID", nullable = false)
  private Long entityId;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "AUTHOR_ID")
  private User author;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "RECIPIENT_ID")
  private User recipient;

  @Transient
  @Builder.Default
  private Boolean isOld = true;

  public Class<? extends BaseEntity> getEntityClass() {
    return switch (getNotificationType()) {
      case POST -> Post.class;
      case FOLLOW -> FollowRequest.class;
      case MESSAGE -> ChatChannel.class;
      case REVIEW -> Review.class;
    };
  }

  public NotificationResponse toResponse() {
    return NotificationResponse.fromEntity(this);
  }
}