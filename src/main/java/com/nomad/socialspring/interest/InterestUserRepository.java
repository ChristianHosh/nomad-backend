package com.nomad.socialspring.interest;

import com.nomad.socialspring.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface InterestUserRepository extends JpaRepository<UserInterest, InterestUserId> {

  @Query("select i from UserInterest i where i.user = :user and i.interest in :interests")
  List<UserInterest> findByUserAndInterestIn(@Param("user") User user, @Param("interests") Collection<Interest> interests);
}