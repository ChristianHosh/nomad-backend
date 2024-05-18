package com.nomad.socialspring.location;

import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.review.ReviewRequest;
import com.nomad.socialspring.review.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
@ResponseStatus(HttpStatus.OK)
public class LocationController {

  private final LocationService locationService;

  @GetMapping("")
  public Page<LocationResponse> getLocations(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size,
          @RequestParam(name = "query", defaultValue = "") String query
  ) {
    return locationService.getLocations(page, size, query);
  }

  @GetMapping("/{id}")
  public LocationResponse getLocation(@PathVariable("id") Long id) {
    return locationService.getLocation(id);
  }

  @GetMapping("/{id}/trips")
  public Page<PostResponse> getTrips(
          @PathVariable("id") Long id,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return locationService.getTrips(id, page, size);
  }

  @GetMapping("/{id}/reviews")
  public Page<ReviewResponse> getReviews(
          @PathVariable(name = "id") Long locationId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return locationService.getReviews(locationId, page, size);
  }

  @PostMapping("/{id}/reviews")
  public ReviewResponse createReview(
          @PathVariable(name = "id") Long locationId,
          @RequestBody ReviewRequest reviewRequest
  ) {
    return locationService.createReview(locationId, reviewRequest);
  }

}
