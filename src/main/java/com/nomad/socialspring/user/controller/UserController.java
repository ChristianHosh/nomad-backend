package com.nomad.socialspring.user.controller;

import com.nomad.socialspring.common.ResponseOk;
import com.nomad.socialspring.user.dto.FollowRequestResponse;
import com.nomad.socialspring.user.dto.ProfileRequest;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseOk
    public UserResponse getUser(
            @PathVariable(name = "id") Long userId
    ) {
        return userService.getUser(userId);
    }

    @PostMapping("/{id}/follow")
    @ResponseOk
    public UserResponse followUser(
            @PathVariable(name = "id") Long userId
    ) {
        return userService.followUser(userId);
    }

    @DeleteMapping("/{id}/follow")
    @ResponseOk
    public UserResponse unfollowUser(
            @PathVariable(name = "id") Long userId
    ) {
        return userService.unfollowUser(userId);
    }

    @GetMapping("/follow-requests")
    @ResponseOk
    public Page<FollowRequestResponse> getFollowRequests(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size
    ) {
        return userService.getUserFollowRequests(page, size);
    }

    @PostMapping("/follow-requests/{id}")
    @ResponseOk
    public UserResponse acceptFollow(
            @PathVariable(name = "id") Long followRequestId
    ) {
        return userService.acceptFollowRequest(followRequestId);
    }

    @DeleteMapping("/follow-requests/{id}")
    @ResponseOk
    public UserResponse declineFollow(
            @PathVariable(name = "id") Long followRequestId
    ) {
        return userService.declineFollowRequest(followRequestId);
    }

    @GetMapping("/{id}/followers")
    @ResponseOk
    public Page<UserResponse> getUserFollowers(
            @PathVariable(name = "id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size
    ) {
        return userService.getUserFollowers(userId, page, size);
    }

    @GetMapping("/{id}/mutual")
    @ResponseOk
    public Page<UserResponse> getUserMutual(
            @PathVariable(name = "id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size
    ) {
        return userService.getUserMutualFollowings(userId, page, size);
    }

    @GetMapping("/{id}/followings")
    @ResponseOk
    public Page<UserResponse> getUserFollowings(
            @PathVariable(name = "id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size
    ) {
        return userService.getUserFollowings(userId, page, size);
    }

    @PutMapping("/profile")
    @ResponseOk
    public UserResponse updateProfileInfo(
            @RequestBody @Valid ProfileRequest profileRequest
    ) {
        return userService.updateProfileInfo(profileRequest);
    }

    @PutMapping("/profile-image")
    @ResponseOk
    public UserResponse updateProfileImage(
            @RequestParam MultipartFile imageFile
    ) {
        return userService.updateProfileImage(imageFile);
    }

    @DeleteMapping("/profile-image")
    @ResponseOk
    public UserResponse deleteProfileImage() {
        return userService.deleteProfileImage();
    }

    @GetMapping("/suggested")
    @ResponseOk
    public List<UserResponse> getSuggestedUsers() {
        return userService.getSuggestedUsers();
    }
}
