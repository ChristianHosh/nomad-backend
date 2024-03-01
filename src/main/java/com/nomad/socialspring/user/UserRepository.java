package com.nomad.socialspring.user;

import com.nomad.socialspring.chat.ChatChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where upper(u.username) = upper(:username)")
  Optional<User> findByUsername(@Param("username") String username);

  @Query("select u from User u where upper(u.email) = upper(:email)")
  Optional<User> findByEmail(@Param("email") String email);

  @Query("select (count(u) > 0) from User u where upper(u.username) = upper(:username)")
  boolean existsByUsername(@Param("username") String username);

  @Query("select (count(u) > 0) from User u where upper(u.email) = upper(:email)")
  boolean existsByEmail(@Param("email") String email);

  @Query("""
              select u from User u inner join u.userChatChannels userChatChannels
              where userChatChannels.chatChannel = :chatChannel
          """)
  Page<User> findByUserChatChannels_ChatChannel(@Param("chatChannel") ChatChannel chatChannel, Pageable pageable);

  @Query(value = """
              SELECT * FROM T_USER U INNER JOIN T_USER_FOLLOWERS UF ON U.id = UF.user_1_id
              WHERE UF.user_2_id = :id
          """, nativeQuery = true)
  Page<User> findByFollowings_Id(@Param("id") Long id, Pageable pageable);

  @Query(value = """
              SELECT * FROM T_USER U INNER JOIN T_USER_FOLLOWERS UF ON U.id = UF.user_2_id
              WHERE UF.user_1_id = :id
          """, nativeQuery = true)
  Page<User> findByFollowers_Id(@Param("id") Long id, Pageable pageable);

  @Query("select u from User u inner join u.likedPosts likedPosts where likedPosts.id = :id")
  Page<User> findByLikedPosts_Id(@Param("id") Long id, Pageable pageable);

  @Query("select u from User u inner join u.likedComments likedComments where likedComments.id = :id")
  Page<User> findByLikedComments_Id(@Param("id") Long id, Pageable pageable);

  @Query("select u from User u inner join u.trips trips where trips.id = :id")
  Page<User> findByTrips_Id(@Param("id") Long id, Pageable pageable);

  @Query(value = """
              SELECT * FROM T_USER U INNER JOIN T_BLOCKED_USERS BU ON U.id = BU.user_2_id
              WHERE BU.user_1_id = :id
          """, nativeQuery = true)
  Page<User> findByBlockedUsersById(@Param("id") Long id, Pageable pageable);

  @Query("""
          select u from User u
          where (
            (
              :query is not null and (
                u.username ilike concat('%',:query ,'%')  or
                u.profile.displayName ilike concat('%',:query ,'%')
              )
            ) and (
              (:currentUser is null) or (
                (:currentUser not member of u.blockedUsers)
              )
            ) and (
              ((:excludeSelf = false) or (:currentUser is not null and u <> :currentUser))
            )
          )
          """)
  Page<User> findBySearchParamExcludeBlocked(@Param("query") String query, @Param("currentUser") User currentUser,
                                             @Param("excludeSelf") boolean excludeSelf,
                                             Pageable pageable);

}