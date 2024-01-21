package com.nomad.socialspring.notification;

import com.nomad.socialspring.comment.model.Comment;
import com.nomad.socialspring.notification.model.Notification;
import com.nomad.socialspring.notification.model.NotificationType;
import com.nomad.socialspring.notification.repo.NotificationRepository;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.user.model.FollowRequest;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private static final String FOLLOW_CONTENT = "has requested to follow you";
    public static final String COMMENT_CONTENT = "has left a comment on your post";
    public static final String POST_LIKE_CONTENT = "has liked your post";
    public static final String COMMENT_LIKE_CONTENT = "has liked your comment";

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

    public void notifyPostLike(Post post, User user) {
        notify(
                user,
                post.getAuthor(),
                POST_LIKE_CONTENT,
                post.getId(),
                NotificationType.POST
        );
    }

    public void notifyCommentLike(Post post, User user) {
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

        repository.save(Notification.builder()
                .author(author)
                .recipient(recipient)
                .content(content)
                .entityId(entityId)
                .notificationType(type)
                .build());
    }
}
