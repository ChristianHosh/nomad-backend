package com.nomad.socialspring.comment.controller;

import com.nomad.socialspring.comment.dto.CommentRequest;
import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.comment.service.CommentService;
import com.nomad.socialspring.common.ResponseOk;
import com.nomad.socialspring.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PutMapping("/{id}")
    @ResponseOk
    public CommentResponse updateComment(
            @PathVariable(name = "id") Long commentId,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        return commentService.updateComment(commentId, commentRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseOk
    public CommentResponse deleteComment(
            @PathVariable(name = "id") Long commentId
    ) {
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/{id}/likes")
    @ResponseOk
    public Page<UserResponse> getCommentLikes(
            @PathVariable(name = "id") Long commentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size
    ) {
        return commentService.getCommentLikes(commentId, page, size);
    }

    @PostMapping("/{id}/likes")
    @ResponseOk
    public CommentResponse likeComment(
            @PathVariable(name = "id") Long commentId
    ) {
        return commentService.likeComment(commentId);
    }

    @DeleteMapping("/{id}/likes")
    @ResponseOk
    public CommentResponse unlikeComment(
            @PathVariable(name = "id") Long commentId
    ) {
        return commentService.unlikeComment(commentId);
    }
}
