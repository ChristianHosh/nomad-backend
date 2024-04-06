package com.nomad.socialspring.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("select p from Post p order by p.zScore DESC")
  Page<Post> findByOrderByScoreDesc(Pageable pageable);
}