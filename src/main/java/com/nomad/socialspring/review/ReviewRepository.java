package com.nomad.socialspring.review;

import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  @Query("select r from Review r where r.recipient = :recipient")
  Page<Review> findByRecipient(@Param("recipient") User recipient, Pageable pageable);
}