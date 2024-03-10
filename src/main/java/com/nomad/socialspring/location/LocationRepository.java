package com.nomad.socialspring.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

  @Query("""
          select l from Location l
          where l.name ilike concat('%', :query, '%') or
          l.belongsTo.name ilike concat('%', :query, '%')
          order by coalesce(l.belongsTo.id, l.id)
        """)
  Page<Location> findByNameContains(@Param("query") String query, Pageable pageable);
}