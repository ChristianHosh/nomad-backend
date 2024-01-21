package com.nomad.socialspring.post.controller;

import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(
            @ModelAttribute @Valid PostRequest request,
            @RequestParam(name = "imageFiles", required = false) MultipartFile[] imageFiles
    ) {
        // should take multipart file as parameter as an image
        return postService.createPost(request, Arrays.asList(imageFiles));
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getPost(
            @PathVariable(name = "id") Long postId
    ) {
        return postService.getPost(postId);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PostResponse updatePost(
            @PathVariable(name = "id") Long postId,
            @ModelAttribute @Valid PostRequest request
    ) {
        return postService.updatePost(postId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PostResponse deletePost(
            @PathVariable(name = "id") Long postId
    ) {
        return postService.deletePost(postId);
    }
}
