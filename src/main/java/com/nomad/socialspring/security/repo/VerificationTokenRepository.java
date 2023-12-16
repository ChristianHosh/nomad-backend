package com.nomad.socialspring.security.repo;

import com.nomad.socialspring.security.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("select v from VerificationToken v where v.token = :token")
    Optional<VerificationToken> findByToken(@Param("token") String token);
}