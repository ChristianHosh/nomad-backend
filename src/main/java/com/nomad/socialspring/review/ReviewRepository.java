package com.nomad.socialspring.review;

import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  @Query("select r from Review r where r.recipient = :recipient")
  Page<Review> findByRecipient(@Param("recipient") User recipient, Pageable pageable);

  @Query("select r from Review r where r.location = :location")
  Page<Review> findByLocation(@Param("location") Location location, Pageable of);

  @Query("select r from Review r where r.location = :location and r.author = :author")
  Optional<Review> findByLocationAndAuthor(Location location, User author);

  @Query("select r from Review r where r.author = :author and r.recipient = :recipient")
  Optional<Review> findByAuthorAndRecipient(User author, User recipient);
}