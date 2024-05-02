package com.nomad.socialspring.trip;

import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class TripController {

  private final TripService tripService;

  @GetMapping("/mine")
  public Page<PostResponse> getMyTrips(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return tripService.getMyTrips(page, size);
  }

  @PutMapping("/{id}/went")
  public TripResponse updateTripStatusAsWent(
          @PathVariable(name = "id") Long tripId
  ) {
    return tripService.updateTripStatusAsWent(tripId);
  }



  @GetMapping("/{id}/participants")
  public Page<UserResponse> getTripParticipants(
          @PathVariable(name = "id") Long tripId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return tripService.getUsersInTrip(tripId, page, size);
  }

  @PostMapping("/{id}/participants")
  public TripResponse addTripParticipant(
          @PathVariable(name = "id") Long tripId
  ) {
    return tripService.joinTrip(tripId);
  }

  @DeleteMapping("/{id}/participants")
  public TripResponse removeTripParticipant(
          @PathVariable(name = "id") Long tripId
  ) {
    return tripService.leaveTrip(tripId);
  }
}
