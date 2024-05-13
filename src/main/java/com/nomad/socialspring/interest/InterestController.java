package com.nomad.socialspring.interest;

import com.nomad.socialspring.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class InterestController {

  private final InterestService interestService;

  @GetMapping("")
  public Page<InterestResponse> getInterests(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size,
          @RequestParam(name = "name", defaultValue = "") String name
  ) {
    return interestService.getInterests(page, size, name);
  }

  @GetMapping("/{id}")
  public InterestResponse getInterest(@PathVariable Long id) {
    return interestService.getInterest(id);
  }

  @GetMapping("/{id}/posts")
  public Page<PostResponse> getInterestPosts(
          @PathVariable(name = "id") Long interestId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return interestService.getInterestPosts(interestId, page, size);
  }
}
