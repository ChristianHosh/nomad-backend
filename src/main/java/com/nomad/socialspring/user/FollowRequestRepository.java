package com.nomad.socialspring.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
  @Query("select f from FollowRequest f where f.toUser = :toUser")
  Page<FollowRequest> findByToUser(@Param("toUser") User toUser, Pageable pageable);


  @Query("select f from FollowRequest f where f.fromUser = :author and f.toUser = :recipient")
  Optional<FollowRequest> findByAuthorAndRecipient(User author, User recipient);
}