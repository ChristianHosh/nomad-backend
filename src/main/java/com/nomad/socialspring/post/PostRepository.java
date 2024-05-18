package com.nomad.socialspring.post;

import com.nomad.socialspring.interest.Interest;
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
            ((p.isPrivate = false) or
            (:user is not null and (:user in elements(p.author.followers) or :user = p.author))) and
            (:user not member of p.author.blockedUsers)
          order by p.zScore desc nulls last, p.createdOn desc
          """)
  Page<Post> findPosts(User user, Pageable pageable);

  @Query("""
          select p from Post p
          where (:user is not null and (:user in elements(p.author.followers) or :user = p.author)) and
            (:user not member of p.author.blockedUsers)
          order by p.zScore desc nulls last, p.createdOn desc
          """)
  Page<Post> findFollowingsPosts(User user, Pageable pageable);

  @Query("""
            select p from Post p
            where
              (p.trip is not null and (p.trip.location = :location or p.trip.location.belongsTo = :location)) and
              (
                (p.isPrivate = false) or
                (:user is not null and (:user in elements(p.author.followers) or :user = p.author))
              ) and
            (:user not member of p.author.blockedUsers)
            order by p.zScore desc nulls last, p.createdOn desc
          """)
  Page<Post> findLocalTrips(User user, Location location, Pageable pageable);

  @Query("""
            select p from Post p
            where
              (p.author = :user) and
              (
                (p.isPrivate = false) or
                (:currentUser is not null and (:currentUser in elements(p.author.followers) or :currentUser = p.author))
              ) and
            (:user not member of p.author.blockedUsers)
            order by p.createdOn DESC
          """)
  Page<Post> findByUser(User user, User currentUser, Pageable pageable);

  @Query("""
          select p from Post p
            inner join UserPostInteraction pi on p.id = pi.post.id
          where :user in elements(p.favorites) and
          (
             (p.isPrivate = false) or
             (:user in elements(p.author.followers) or :user = p.author)
          ) and
          pi.event = com.nomad.socialspring.recommender.Event.FAVORITE and
          pi.user = :user and
            (:user not member of p.author.blockedUsers)
          order by pi.createdOn
          """)
  Page<Post> findAllByFavorites(User user, Pageable pageable);

  @Query("""
          select p from Post p
            inner join TripUser tu on p.trip.id = tu.trip.id
          where
          p.trip is not null and
          (
             (p.isPrivate = false) or
             (:user in elements(p.author.followers) or :user = p.author)
          ) and
          (
            (tu.user = :user)
          )
          order by p.trip.startDate asc
          """)
  Page<Post> findTripsByUser(User user, Pageable pageable);

  @Query("""
          select p from Post p
            inner join TripUser tu on p.trip.id = tu.trip.id
          where (p.trip is not null) and
                (p.trip.endDate > current date) and
                (:user = tu.user) and
                (:user not member of p.author.blockedUsers)
          order by p.trip.startDate asc
          """)
  Page<Post> findUpcomingTrips(User user, Pageable pageable);

  @Query("""
          select p from Post p
          where
            ((p.isPrivate = false) or
            (:user is not null and (:user in elements(p.author.followers) or :user = p.author))) and
            (:user not member of p.author.blockedUsers) and
            (p.trip is not null and p.trip.location = :location)
          order by p.zScore desc nulls last, p.createdOn desc
          """)
  Page<Post> findByLocation(Location location, User user, Pageable pageable);

  @Query("""
          select p from Post p
          where
            ((p.isPrivate = false) or
            (:user is not null and (:user in elements(p.author.followers) or :user = p.author))) and
            (:user not member of p.author.blockedUsers) and
            (:interest in elements(p.interests))
          order by p.zScore desc nulls last, p.createdOn desc
          """)
  Page<Post> findByInterest(Interest interest, User user, Pageable pageable);
}