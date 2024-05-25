package com.nomad.socialspring.trip;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.location.Location;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripFacade {

  private final TripRepository repository;

  public Trip save(@NotNull @Valid TripRequest tripRequest, Location location) {
    BDate startDate = BDate.valueOf(tripRequest.startDate());
    BDate endDate = BDate.valueOf(tripRequest.endDate());
    if (startDate.after(endDate))
      throw BxException.badRequest(Trip.class, "end date", "can't be before start date");

    return save(TripMapper.requestToEntity(tripRequest, location));
  }

  public Trip save(Trip trip) {
    return repository.save(trip);
  }

  public Trip findById(Long id) {
    return repository.findById(id)
        .orElseThrow(BxException.xNotFound(Trip.class, id));
  }
}
