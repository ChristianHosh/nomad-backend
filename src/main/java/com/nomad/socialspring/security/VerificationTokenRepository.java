package com.nomad.socialspring.security;

import com.nomad.socialspring.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("select v from VerificationToken v where v.token = :token")
    Optional<VerificationToken> findByToken(@Param("token") String token);

    @Query("select v from VerificationToken v where v.user = :user")
    Optional<VerificationToken> findByUser(@Param("user") User user);
}