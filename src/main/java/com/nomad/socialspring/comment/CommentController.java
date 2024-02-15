package com.nomad.socialspring.comment;

import com.nomad.socialspring.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CommentResponse updateComment(
          @PathVariable(name = "id") Long commentId,
          @RequestBody @Valid CommentRequest commentRequest
  ) {
    return commentService.updateComment(commentId, commentRequest);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public CommentResponse deleteComment(
          @PathVariable(name = "id") Long commentId
  ) {
    return commentService.deleteComment(commentId);
  }

  @GetMapping("/{id}/likes")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getCommentLikes(
          @PathVariable(name = "id") Long commentId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return commentService.getCommentLikes(commentId, page, size);
  }

  @PostMapping("/{id}/likes")
  @ResponseStatus(HttpStatus.OK)
  public CommentResponse likeComment(
          @PathVariable(name = "id") Long commentId
  ) {
    return commentService.likeComment(commentId);
  }

  @DeleteMapping("/{id}/likes")
  @ResponseStatus(HttpStatus.OK)
  public CommentResponse unlikeComment(
          @PathVariable(name = "id") Long commentId
  ) {
    return commentService.unlikeComment(commentId);
  }
}
