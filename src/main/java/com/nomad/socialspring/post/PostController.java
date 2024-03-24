package com.nomad.socialspring.post;

import com.nomad.socialspring.comment.CommentRequest;
import com.nomad.socialspring.comment.CommentResponse;
import com.nomad.socialspring.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public PostResponse createPost(
          @ModelAttribute @Valid PostRequest request,
          @RequestParam(name = "imageFiles", required = false) MultipartFile[] imageFiles
  ) {
    return postService.createPost(request, Arrays.asList(imageFiles));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PostResponse getPost(
          @PathVariable(name = "id") Long postId
  ) {
    return postService.getPost(postId);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PostResponse updatePost(
          @PathVariable(name = "id") Long postId,
          @ModelAttribute @Valid PostRequest request
  ) {
    return postService.updatePost(postId, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PostResponse deletePost(
          @PathVariable(name = "id") Long postId
  ) {
    return postService.deletePost(postId);
  }

  @GetMapping("/{id}/comments")
  @ResponseStatus(HttpStatus.OK)
  public Page<CommentResponse> getPostComments(
          @PathVariable(name = "id") Long postId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return postService.getPostComments(postId, page, size);
  }

  @PostMapping("/{id}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse createComment(
          @PathVariable(name = "id") Long postId,
          @RequestBody @Valid CommentRequest commentRequest
  ) {
    return postService.createComment(postId, commentRequest);
  }

  @GetMapping("/{id}/likes")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getPostLikes(
          @PathVariable(name = "id") Long postId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return postService.getPostLikes(postId, page, size);
  }

  @PostMapping("/{id}/likes")
  @ResponseStatus(HttpStatus.CREATED)
  public PostResponse likePost(
          @PathVariable(name = "id") Long postId
  ) {
    return postService.likePost(postId);
  }

  @DeleteMapping("/{id}/likes")
  @ResponseStatus(HttpStatus.CREATED)
  public PostResponse unlikePost(
          @PathVariable(name = "id") Long postId
  ) {
    return postService.unlikePost(postId);
  }

}
