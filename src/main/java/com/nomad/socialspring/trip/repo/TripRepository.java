package com.nomad.socialspring.trip.repo;

import com.nomad.socialspring.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}