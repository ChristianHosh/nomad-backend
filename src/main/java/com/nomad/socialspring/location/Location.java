package com.nomad.socialspring.location;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.review.Review;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.trip.TripUser;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_LOCATION")
public class Location extends BaseEntity {

  public Location(String name, Location belongsTo) {
    this.name = name;
    this.belongsTo = belongsTo;
    this.locations = new LinkedHashSet<>();
  }

  @Column(name = "NAME", nullable = false, unique = true)
  private String name;

  @Column(name = "IMAGE_URL", length = 512)
  private String imageUrl;

  @Column(name = "ABOUT", length = 512)
  private String about;

  @ManyToOne
  @JoinColumn(name = "BELONGS_TO_ID")
  private Location belongsTo;

  @OneToMany(mappedBy = "belongsTo")
  @Builder.Default
  private Set<Location> locations = new LinkedHashSet<>();

  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Review> reviews = new LinkedHashSet<>();

  public Location(String name, String imageUrl, String about, Location belongsTo, Set<Location> locations) {
    this(name, imageUrl, about, belongsTo, locations, new LinkedHashSet<>());
  }

  public String getFullName() {
    return belongsTo == null ? name : name + ", " + belongsTo.getFullName();
  }

  @Override
  public void setId(Long id) {
    super.setId(id);
  }

  public LocationResponse toResponse(User user) {
    return LocationResponse.fromEntity(this, user);
  }

  public LocationResponse toResponse() {
    return LocationResponse.fromEntity(this);
  }

  public UserResponse.CanReview canBeReviewedBy(User user) {
    if (user != null) {
      if (isReviewedBy(user))
        return UserResponse.CanReview.REVIEWED;

      for (TripUser tripUser : user.getTripUsers()) {
        Trip trip = tripUser.getTrip();
        if (this.equals(trip.getLocation()) && tripUser.getStatus() == TripUser.TripUserStatus.WENT)
          return UserResponse.CanReview.CAN_REVIEW;
      }
    }
    return UserResponse.CanReview.DISABLED;
  }

  private boolean isReviewedBy(User user) {
    if (user == null)
      return false;
    return reviews.stream().anyMatch(review -> Objects.equals(review.getAuthor(), user));
  }
}