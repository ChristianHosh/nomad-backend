package com.nomad.socialspring.recommender;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;


public interface UserPostInteractionRepository extends JpaRepository<UserPostInteraction, Long> {

  @Query("""
      select u from UserPostInteraction u
      where u.user = :user and u.post = :post and u.event = :event
      order by u.createdOn DESC
      """)
  List<UserPostInteraction> findByUserAndPostAndEventOrderByCreatedOnDesc(@Param("user") User user, @Param("post") Post post, @Param("event") Event event);

  @Query("""
        select e.post, sum(e.strength) from UserPostInteraction e
        where (e.createdOn > :date)
        group by e.post
      """)
  List<Object[]> findPostsWithInteractionsAfter(@Param("date") Date date);
}