package com.nomad.socialspring.country.repo;

import com.nomad.socialspring.country.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}