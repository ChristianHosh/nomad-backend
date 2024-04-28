package com.nomad.socialspring.interest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.Future;

public interface InterestRepository extends JpaRepository<Interest, Long> {
  Optional<Interest> findByNameIgnoreCase(String name);

  @Query("select i from Interest i where :name is null or i.name ilike concat('%', :name, '%')")
  Page<Interest> findByNameContains(@Param("name") String name, Pageable pageable);

  @Async
  @Query("""
         select i from Interest i where i.name ilike concat('%', :query, '%')
        """)
  Future<Page<Interest>> searchInterestsAsync(String query, Pageable pageable);
}