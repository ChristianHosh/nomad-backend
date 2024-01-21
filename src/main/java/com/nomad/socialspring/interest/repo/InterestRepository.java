package com.nomad.socialspring.interest.repo;

import com.nomad.socialspring.interest.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByNameIgnoreCase(String name);
}