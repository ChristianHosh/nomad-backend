package com.nomad.socialspring.comment;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.recommender.PostEventHandler;
import com.nomad.socialspring.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentFacade commentFacade;
  private final UserFacade userFacade;
  private final NotificationFacade notificationFacade;
  private final PostEventHandler postEventHandler;

  public CommentResponse deleteComment(Long commentId) {
    Comment comment = commentFacade.findById(commentId);
    User currentUser = userFacade.getCurrentUser();
    if (comment.canBeDeletedBy(currentUser)) {
      commentFacade.delete(comment);
      postEventHandler.deleteComment(currentUser, comment.getPost());
      return comment.toResponse();
    }

    throw BxException.unauthorized(currentUser);
  }

  public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
    Comment comment = commentFacade.findById(commentId);
    User currentUser = userFacade.getCurrentUser();
    if (comment.canBeModifiedBy(currentUser)) {
      String content = commentRequest.content();
      comment.setContent(commentRequest.content());
      List<User> mentionedUsers = userFacade.getMentionedUsersFromContent(content);
      comment = commentFacade.save(comment);

      notificationFacade.notifyMentions(mentionedUsers, comment);
      return comment.toResponse(currentUser);
    }

    throw BxException.unauthorized(currentUser);
  }

  public Page<UserResponse> getCommentLikes(Long commentId, int page, int size) {
    Comment comment = commentFacade.findById(commentId);

    return userFacade.findAllByCommentLiked(comment.getId(), page, size)
        .map(User::toResponse);
  }

  public CommentResponse likeComment(Long commentId) {
    Comment comment = commentFacade.findById(commentId);
    User currentUser = userFacade.getCurrentUser();

    if (comment.getLikes().add(currentUser))
      notificationFacade.notifyCommentLike(comment.getPost(), currentUser);
    else
      throw BxException.hardcoded(BxException.X_COULD_NOT_LIKE_COMMENT, currentUser);

    postEventHandler.likeComment(currentUser, comment.getPost());
    return commentFacade.save(comment).toResponse(currentUser);
  }

  public CommentResponse unlikeComment(Long commentId) {
    Comment comment = commentFacade.findById(commentId);
    User currentUser = userFacade.getCurrentUser();

    if (!comment.getLikes().remove(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_UNLIKE_COMMENT, currentUser);

    postEventHandler.unlikeComment(currentUser, comment.getPost());
    return commentFacade.save(comment).toResponse(currentUser);
  }
}
