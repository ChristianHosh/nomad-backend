package com.nomad.socialspring.country;

import com.nomad.socialspring.error.BxException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryFacade {

  private final CountryRepository repository;

  public Country findById(Long id) {
    return repository.findById(id)
            .orElseThrow(BxException.xNotFound(Country.class, id));
  }
}
