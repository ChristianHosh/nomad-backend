package com.nomad.socialspring.user;

import com.nomad.socialspring.chat.ChatChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

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
              where userChatChannels.chatChannel = :chatChannel and
              (
                (u.username ilike concat('%', :query, '%')) or
                (u.profile.displayName ilike concat('%', :query, '%'))
              )
          """)
  Page<User> findByUserChatChannels_ChatChannel(@Param("chatChannel") ChatChannel chatChannel, String query, Pageable pageable);

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

  @Query("select u from User u inner join TripUser tu on u.id = tu.user.id where tu.trip.id = :id")
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

  @Query("select u from User u where u.id in :ids")
  List<User> findByIdIn(@Param("ids") Collection<Long> ids);

  @Query("select u from User u where u.username in :usernames")
  List<User> findByUsernameIn(@Param("usernames") Collection<String> usernames);

  @Query("""
              select u from User u
              where (
                :user not member of u.blockedUsers and
                :user not member of u.followers and
                u not in :excludedUsers
              )
              order by u.createdOn desc
          """)
  List<User> findByRandomAndExclude(@Param("user") User user, @Param("excludedUsers") Collection<User> excludedUsers, Pageable pageable);

  @Async
  @Query("""
          select u from User u
          where (u.username ilike concat('%',:query ,'%') or u.profile.displayName ilike concat('%',:query ,'%')) and
                (:user is null or :user != u) and
                (:user not member of u.blockedUsers)
          """)
  Future<Page<User>> searchUsersAsync(String query, User user, Pageable pageable);


}