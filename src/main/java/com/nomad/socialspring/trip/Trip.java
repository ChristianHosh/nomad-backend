package com.nomad.socialspring.trip;

import com.nomad.socialspring.chat.ChatChannel;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_TRIP")
public class Trip extends BaseEntity {

  @Column(name = "START_DATE", nullable = false)
  private Date startDate;

  @Column(name = "END_DATE", nullable = false)
  private Date endDate;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "LOCATION_ID", nullable = false)
  private Location location;

  @OneToOne(mappedBy = "trip", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false, orphanRemoval = true)
  private Post post;

  @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<TripUser> tripUsers = new LinkedHashSet<>();

  @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  private ChatChannel chatChannel;

  private TripUser newTripUser(User user) {
    return new TripUser(
            new TripUser.TripUserId(this.getId(), user.getId()),
            this,
            user,
            TripUser.TripUserStatus.JOINED
    );
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean addParticipant(User user) {
    return addParticipant(user, false);
  }

  public boolean addParticipant(User user, boolean skipChannel) {
    if (user == null)
      return false;
    if (skipChannel)
      return tripUsers.add(newTripUser(user));
    return tripUsers.add(newTripUser(user)) && chatChannel.addUser(user);
  }

  public boolean removeParticipant(User user) {
    if (user == null)
      return false;
    return tripUsers.removeIf(tripUser -> Objects.equals(tripUser.getUser(), user)) &&
            chatChannel.removeUser(user);
  }

  public int getNumberOfParticipants() {
    return tripUsers.size();
  }

  public TripResponse toResponse(User currentUser) {
    return new TripResponse(this, currentUser);
  }

  public TripUser findTripUser(User user) {
    return tripUsers.stream()
            .filter(tu -> Objects.equals(tu.getUser(), user))
            .findFirst()
            .orElse(null);
  }
}