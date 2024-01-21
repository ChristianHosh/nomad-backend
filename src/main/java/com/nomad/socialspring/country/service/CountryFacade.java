package com.nomad.socialspring.country.service;

import com.nomad.socialspring.country.model.Country;
import com.nomad.socialspring.country.repo.CountryRepository;
import com.nomad.socialspring.error.exceptions.BxException;
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
