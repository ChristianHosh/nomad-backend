package com.nomad.socialspring.interest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {
  Optional<Interest> findByNameIgnoreCase(String name);

  @Query("select i from Interest i where :name is null or i.name like :name")
  Page<Interest> findByNameContains(@Param("name") String name, Pageable pageable);
}