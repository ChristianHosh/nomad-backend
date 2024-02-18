package com.nomad.socialspring.recommender;

import com.nomad.socialspring.country.Country;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


public interface UserPostInteractionRepository extends JpaRepository<UserPostInteraction, Long> {

  @Query("""
            select e.post from UserPostInteraction e
            where e.post.isPrivate = false or (:user is not null and :user in elements(e.post.author.followers))
            group by e.post
            order by (sum(e.strength) + (e.post.recencyScore)) desc
          """)
  Page<Object[]> findPosts(@Param("user") User user, Pageable pageable);

  @Query("""
            select e.post from UserPostInteraction e
            where (e.post.isPrivate = false or (:user is not null and :user in elements(e.post.author.followers))) and
                  (e.post.trip is not null and e.post.trip.country = :country)
            group by e.post
            order by (sum(e.strength) + (e.post.recencyScore)) desc
          """)
  Page<Object[]> findPostsByCountry(@Param("user") User user, Country country, Pageable pageable);

  @Query("select count(distinct u) from UserPostInteraction u")
  long countDistinct();

  @Query("""
          select u from UserPostInteraction u
          where u.user = :user and u.post = :post and u.event = :event
          order by u.createdOn DESC
          """)
  List<UserPostInteraction> findByUserAndPostAndEventOrderByCreatedOnDesc(@Param("user") User user, @Param("post") Post post, @Param("event") Event event);

  @Query("""
            select e.post from UserPostInteraction e
            where (e.post.isPrivate = false or (:user is not null and :user in elements(e.post.author.followers))) and
                  (e.createdOn > :date)
            group by e.post
            order by (sum(e.strength) + (e.post.recencyScore)) desc
          """)
  Page<Object[]> findPostsByTrendingAfter(@Param("user") User user, @Param("date") Date date, Pageable pageable);
  
  @Query("""
          select e.post, sum(e.strength) from UserPostInteraction e
          where (e.createdOn > :date)
          group by e.post
        """)
  List<Object[]> findPostsWithInteractionsAfter(@Param("date") Date date);
  
}