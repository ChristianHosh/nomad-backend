package com.nomad.socialspring.comment.service;

import com.nomad.socialspring.comment.dto.CommentRequest;
import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.comment.model.Comment;
import com.nomad.socialspring.comment.model.CommentMapper;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.notification.service.NotificationFacade;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.model.UserMapper;
import com.nomad.socialspring.user.service.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentFacade commentFacade;
    private final UserFacade userFacade;
    private final NotificationFacade notificationFacade;

    public CommentResponse deleteComment(Long commentId) {
        Comment comment = commentFacade.findById(commentId);
        User currentUser = userFacade.getAuthenticatedUser();
        if (comment.canBeDeletedBy(currentUser)) {
            commentFacade.delete(comment);
            return CommentMapper.entityToResponse(comment);
        }

        throw BxException.unauthorized(currentUser);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentFacade.findById(commentId);
        User currentUser = userFacade.getAuthenticatedUser();
        if (comment.canBeModifiedBy(currentUser)) {
            comment.setContent(commentRequest.content());
            return CommentMapper.entityToResponse(commentFacade.save(comment));
        }

        throw BxException.unauthorized(currentUser);
    }

    public Page<UserResponse> getCommentLikes(Long commentId, int page, int size) {
        Comment comment = commentFacade.findById(commentId);

        return userFacade.findAllByCommentLiked(comment.getId(), page, size).map(UserMapper::entityToResponse);
    }

    public CommentResponse likeComment(Long commentId) {
        Comment comment = commentFacade.findById(commentId);
        User currentUser = userFacade.getAuthenticatedUser();

        if (comment.getLikes().add(currentUser))
            notificationFacade.notifyCommentLike(comment.getPost(), currentUser);

        return CommentMapper.entityToResponse(commentFacade.save(comment));
    }

    public CommentResponse unlikeComment(Long commentId) {
        Comment comment = commentFacade.findById(commentId);
        User currentUser = userFacade.getAuthenticatedUser();

        comment.getLikes().remove(currentUser);

        return CommentMapper.entityToResponse(commentFacade.save(comment));
    }
}
