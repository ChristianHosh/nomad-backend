package com.nomad.socialspring.user.repo;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.user.model.User;
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

    @Query("select u from User u inner join u.followings followings where followings.id = :id")
    Page<User> findByFollowings_Id(@Param("id") Long id, Pageable pageable);

    @Query("select u from User u inner join u.followers followers where followers.id = :id")
    Page<User> findByFollowers_Id(@Param("id") Long id, Pageable pageable);

}