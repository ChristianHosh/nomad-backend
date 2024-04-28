package com.nomad.socialspring.location;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

  private final LocationFacade locationFacade;


  public Page<LocationResponse> getLocations(int page, int size, String query) {
    return locationFacade
            .getLocations(page, size, query)
            .map(LocationResponse::fromEntity);
  }

  public LocationResponse getLocation(Long id) {
    return locationFacade.findById(id).toResponse();
  }
}
