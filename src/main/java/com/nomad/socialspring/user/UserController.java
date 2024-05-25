package com.nomad.socialspring.user;

import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.review.ReviewRequest;
import com.nomad.socialspring.review.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse getUser(
      @PathVariable(name = "id") Long userId
  ) {
    return userService.getUser(userId);
  }

  @GetMapping("/@{username}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse getUserByUsername(
      @PathVariable(name = "username") String username
  ) {
    return userService.getUser(username);
  }

  @PostMapping("/{id}/follow")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse followUser(
      @PathVariable(name = "id") Long userId
  ) {
    return userService.followUser(userId);
  }

  @DeleteMapping("/{id}/follow")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse unfollowUser(
      @PathVariable(name = "id") Long userId
  ) {
    return userService.unfollowUser(userId);
  }

  @GetMapping("/follow-requests")
  @ResponseStatus(HttpStatus.OK)
  public Page<FollowRequestResponse> getFollowRequests(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getUserFollowRequests(page, size);
  }

  @PostMapping("/follow-requests/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse acceptFollow(
      @PathVariable(name = "id") Long followRequestId
  ) {
    return userService.acceptFollowRequest(followRequestId);
  }

  @DeleteMapping("/follow-requests/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse declineFollow(
      @PathVariable(name = "id") Long followRequestId
  ) {
    return userService.declineFollowRequest(followRequestId);
  }

  @GetMapping("/{id}/followers")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getUserFollowers(
      @PathVariable(name = "id") Long userId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getUserFollowers(userId, page, size);
  }

  @GetMapping("/{id}/mutual")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getUserMutual(
      @PathVariable(name = "id") Long userId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getUserMutualFollowings(userId, page, size);
  }

  @GetMapping("/{id}/followings")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getUserFollowings(
      @PathVariable(name = "id") Long userId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getUserFollowings(userId, page, size);
  }

  @GetMapping("/{id}/posts")
  @ResponseStatus(HttpStatus.OK)
  public Page<PostResponse> getUserPosts(
      @PathVariable(name = "id") Long userId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getUserPosts(userId, page, size);
  }

  @PutMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse updateProfileInfo(
      @RequestBody @Valid ProfileRequest profileRequest
  ) {
    return userService.updateProfileInfo(profileRequest);
  }

  @PutMapping("/profile-image")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse updateProfileImage(
      @RequestParam MultipartFile imageFile
  ) {
    return userService.updateProfileImage(imageFile);
  }

  @DeleteMapping("/profile-image")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse deleteProfileImage() {
    return userService.deleteProfileImage();
  }

  @GetMapping("/suggested")
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponse> getSuggestedUsers() {
    return userService.getSuggestedUsers();
  }

  @GetMapping("/info")
  @ResponseStatus(HttpStatus.OK)
  public UserInfoResponse getUserInfo() {
    return userService.getUserInfo();
  }

  @GetMapping("/{id}/reviews")
  @ResponseStatus(HttpStatus.OK)
  public Page<ReviewResponse> getUserReviews(
      @PathVariable(name = "id") Long userId,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getUserReviews(userId, page, size);
  }

  @PostMapping("/{id}/reviews")
  @ResponseStatus(HttpStatus.CREATED)
  public ReviewResponse createUserReview(
      @PathVariable(name = "id") Long userId,
      @RequestBody @Valid ReviewRequest reviewRequest
  ) {
    return userService.createUserReview(userId, reviewRequest);
  }

  @PostMapping("/{id}/block")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse blockUser(
      @PathVariable(name = "id") Long userId
  ) {
    return userService.blockUser(userId);
  }


  @DeleteMapping("/{id}/block")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse unblockUser(
      @PathVariable(name = "id") Long userId
  ) {
    return userService.unblockUser(userId);
  }

  @GetMapping("/blocked")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getBlockedUsers(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getBlockedUsers(page, size);
  }

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getAllUsers(
      @RequestParam(name = "query", defaultValue = "") String query,
      @RequestParam(name = "excludeSelf", defaultValue = "false") boolean excludeSelf,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return userService.getAllUsers(query, excludeSelf, page, size);
  }
}
