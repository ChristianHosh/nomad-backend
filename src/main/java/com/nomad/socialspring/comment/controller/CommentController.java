package com.nomad.socialspring.comment.controller;

import com.nomad.socialspring.comment.dto.CommentRequest;
import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.comment.service.CommentService;
import com.nomad.socialspring.user.dto.UserResponse;
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
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse updateComment(
            @PathVariable(name = "id") Long commentId,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        return commentService.updateComment(commentId, commentRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse deleteComment(
            @PathVariable(name = "id") Long commentId
    ) {
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/{id}/likes")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> getCommentLikes(
            @PathVariable(name = "id") Long commentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size
    ) {
        return commentService.getCommentLikes(commentId, page, size);
    }

    @PostMapping("/{id}/likes")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse likeComment(
            @PathVariable(name = "id") Long commentId
    ) {
        return commentService.likeComment(commentId);
    }

    @DeleteMapping("/{id}/likes")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse unlikeComment(
            @PathVariable(name = "id") Long commentId
    ) {
        return commentService.unlikeComment(commentId);
    }
}
