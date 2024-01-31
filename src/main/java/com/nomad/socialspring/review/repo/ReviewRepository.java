package com.nomad.socialspring.review.repo;

import com.nomad.socialspring.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}