package com.nomad.socialspring.post.controller;

import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createNewPost(
            @ModelAttribute @Valid PostRequest request,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        // should take multipart file as parameter as an image
        return postService.createNewPost(request, imageFile);
    }

}
