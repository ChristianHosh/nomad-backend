package com.nomad.socialspring.location;

import com.nomad.socialspring.error.BxException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationFacade {

  private final LocationRepository repository;

  public Location findById(Long id) {
    return repository.findById(id)
        .orElseThrow(BxException.xNotFound(Location.class, id));
  }

  public Page<Location> getLocations(int page, int size, String query) {
    return repository.findByNameContains(query, PageRequest.of(page, size));
  }
}
