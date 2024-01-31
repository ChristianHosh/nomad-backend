package com.nomad.socialspring.notification.service;

import com.nomad.socialspring.comment.model.Comment;
import com.nomad.socialspring.notification.model.Notification;
import com.nomad.socialspring.notification.model.NotificationType;
import com.nomad.socialspring.notification.repo.NotificationRepository;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.review.model.Review;
import com.nomad.socialspring.trip.model.Trip;
import com.nomad.socialspring.user.model.FollowRequest;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private static final String FOLLOW_CONTENT = "has requested to follow you";
    public static final String COMMENT_CONTENT = "has left a comment on your post";
    public static final String POST_LIKE_CONTENT = "has liked your post";
    public static final String COMMENT_LIKE_CONTENT = "has liked your comment";
    private static final String JOIN_TRIP_CONTENT = "has joined your trip";
    private static final String REVIEW_CONTENT = "has left a review on your profile";

    private final NotificationRepository repository;


    public void notifyPostComment(@NotNull Post post, @NotNull Comment comment) {
        notify(
                comment.getAuthor(),
                post.getAuthor(),
                COMMENT_CONTENT,
                post.getId(),
                NotificationType.POST
        );
    }

    public void notifyPostLike(@NotNull Post post, User user) {
        notify(
                user,
                post.getAuthor(),
                POST_LIKE_CONTENT,
                post.getId(),
                NotificationType.POST
        );
    }

    public void notifyCommentLike(@NotNull Post post, User user) {
        notify(
                user,
                post.getAuthor(),
                COMMENT_LIKE_CONTENT,
                post.getId(),
                NotificationType.POST
        );
    }

    public void notifyFollowRequest(@NotNull FollowRequest followRequest) {
        notify(
                followRequest.getFromUser(),
                followRequest.getToUser(),
                FOLLOW_CONTENT,
                followRequest.getId(),
                NotificationType.FOLLOW
        );
    }

    public void notify(User author, User recipient, String content, Long entityId, NotificationType type) {
        if (Objects.equals(author, recipient))
            return;

        save(Notification.builder()
                .author(author)
                .recipient(recipient)
                .content(content)
                .entityId(entityId)
                .notificationType(type)
                .build());
    }

    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    public Page<Notification> getNotifications(User user, int page, int size) {
        Page<Notification> notifcationPage = repository.findByRecipient(user, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdOn"))));
        notifcationPage.forEach(this::readAndSave);
        return notifcationPage;
    }

    private void readAndSave(@NotNull Notification notification) {
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setIsOld(false);
            save(notification);
        }
    }

    public void deleteFollowNotification(@NotNull FollowRequest followRequest) {
        repository.deleteByFollowRequestId(followRequest.getId());
    }

    public void notifyTripJoin(@NotNull Trip trip, User user) {
        notify(
                user,
                trip.getPost().getAuthor(),
                JOIN_TRIP_CONTENT,
                trip.getPost().getId(),
                NotificationType.POST
        );
    }

    public void notifyUserReview(@NotNull Review review) {
        notify(
                review.getAuthor(),
                review.getRecipient(),
                REVIEW_CONTENT,
                review.getAuthor().getId(),
                NotificationType.REVIEW
        );
    }
}
