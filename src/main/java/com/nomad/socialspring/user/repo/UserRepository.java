package com.nomad.socialspring.user.repo;

import com.nomad.socialspring.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select (count(u) > 0) from User u where upper(u.username) = upper(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);

    @Query("select (count(u) > 0) from User u where upper(u.email) = upper(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
}