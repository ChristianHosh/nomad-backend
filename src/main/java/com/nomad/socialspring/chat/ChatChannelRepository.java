package com.nomad.socialspring.chat;

import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {

  @Query("select c from ChatChannel c where c.uuid = :uuid")
  Optional<ChatChannel> findByUuid(@Param("uuid") UUID uuid);

  @Query("select c from ChatChannel c where c.trip = :trip")
  Optional<ChatChannel> findByTrip(@Param("trip") Trip trip);

  @Query("""
          select distinct c from ChatChannel c
            inner join c.chatChannelUsers chatChannelUsers
            inner join ChatMessage m on m.chatChannel.id = c.id
          where chatChannelUsers.user = :user
          order by max(m.createdOn)
          """)
  Page<ChatChannel> findByChatChannelUsers_User(@Param("user") User user, Pageable pageable);

  @Query("""
         select distinct c from ChatChannel c inner join c.chatChannelUsers ccu
         group by c
         having sum(case when ccu.user in :users then 1 else 0 end) = :usersSize
         """)
  List<ChatChannel> findChatChannelByUsers(@Param("users") Collection<User> users, @Param("usersSize") int usersSize);
}