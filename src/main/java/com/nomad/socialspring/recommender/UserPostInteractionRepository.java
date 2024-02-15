package com.nomad.socialspring.recommender;

import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserPostInteractionRepository extends JpaRepository<UserPostInteraction, Long> {
  
  @SuppressWarnings("All")
  double recencyFactor = 0.05;
  
  @Query("""
    select
      e.post as p,
      max(p.createdOn) as createdTime,
      com.nomad.socialspring.recommender.UserPostInteractionRepository.recencyFactor * (current_timestamp - createdTime) as recencyScore
    from UserPostInteraction e
    where p.isPrivate = false or (:currentUser is not null and :currentUser in p.author.followers)
    group by e.post
    order by (sum(e.strength) + recencyScore) desc
  """)
  Page<Object[]> findPostsByScore(User currentUser, Pageable pageable);
  
}