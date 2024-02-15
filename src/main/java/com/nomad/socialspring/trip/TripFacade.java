package com.nomad.socialspring.trip;

import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.country.Country;
import com.nomad.socialspring.error.BxException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripFacade {

  private final TripRepository repository;

  public Trip save(@NotNull TripRequest tripRequest, Country country) {
    BDate startDate = BDate.valueOf(tripRequest.startDate());
    BDate endDate = BDate.valueOf(tripRequest.endDate());
    if (startDate.after(endDate))
      throw BxException.badRequest(Trip.class, "end date", "can't be before start date");

    return save(TripMapper.requestToEntity(tripRequest, country));
  }

  public Trip save(Trip trip) {
    return repository.save(trip);
  }

  public Trip findById(Long id) {
    return repository.findById(id)
            .orElseThrow(BxException.xNotFound(Trip.class, id));
  }
}
