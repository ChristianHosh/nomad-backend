package com.nomad.socialspring.post;

import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


  @Query("""
          select p from Post p
          where
            (p.isPrivate = false) or
            (:user is not null and (:user in elements(p.author.followers) or :user = p.author))
          order by p.zScore DESC
          """)
  Page<Post> findPosts(User user, Pageable pageable);

  @Query("""
          select p from Post p
          where (:user is not null and (:user in elements(p.author.followers) or :user = p.author))
          order by p.zScore DESC
          """)
  Page<Post> findFollowingsPosts(User user, Pageable pageable);

  @Query("""
          select p from Post p
          where
            (p.trip != null and (p.trip.location = :location or p.trip.location.belongsTo = :location)) and
            (
              (p.isPrivate = false) or
              (:user is not null and (:user in elements(p.author.followers) or :user = p.author))
            )
          order by p.zScore DESC
        """)
  Page<Post> findLocalTrips(User user, Location location, Pageable pageable);

  @Query("""
          select p from Post p
          where
            (p.author = :user) and
            (
              (p.isPrivate = false) or
              (:currentUser is not null and (:currentUser in elements(p.author.followers) or :currentUser = p.author))
            )
          order by p.createdOn DESC
        """)
  Page<Post> findByUser(User user, User currentUser, Pageable pageable);
}