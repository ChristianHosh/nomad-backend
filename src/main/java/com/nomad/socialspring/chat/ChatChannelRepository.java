package com.nomad.socialspring.chat;

import com.nomad.socialspring.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {

  @Query("select c from ChatChannel c where c.uuid = :uuid")
  Optional<ChatChannel> findByUuid(@Param("uuid") UUID uuid);

  @Query("select c from ChatChannel c where c.trip = :trip")
  Optional<ChatChannel> findByTrip(@Param("trip") Trip trip);
}