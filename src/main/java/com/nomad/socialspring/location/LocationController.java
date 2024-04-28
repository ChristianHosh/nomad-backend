package com.nomad.socialspring.location;

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

}
