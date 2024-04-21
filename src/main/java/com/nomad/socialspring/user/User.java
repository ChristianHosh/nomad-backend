package com.nomad.socialspring.user;

import com.nomad.socialspring.chat.ChatChannelUser;
import com.nomad.socialspring.comment.Comment;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.UserInterest;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.review.Review;
import com.nomad.socialspring.trip.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER")
public class User extends BaseEntity {

  @Column(name = "USERNAME", nullable = false, unique = true, length = 30)
  @Size(min = 4, max = 30)
  private String username;

  @Column(name = "PASSWORD", nullable = false, unique = true)
  private String password;

  @Column(name = "EMAIL", nullable = false, unique = true, length = 50)
  @Size(min = 4, max = 50)
  @Email
  private String email;

  @Column(name = "IS_VERIFIED", nullable = false)
  @Builder.Default
  private Boolean isVerified = false;

  @Enumerated
  @Column(name = "ROLE", nullable = false)
  private Role role;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "T_USER_FOLLOWERS",
          joinColumns = @JoinColumn(name = "USER_1_ID"),
          inverseJoinColumns = @JoinColumn(name = "USER_2_ID"))
  @Builder.Default
  private Set<User> followers = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "T_USER_FOLLOWERS",
          joinColumns = @JoinColumn(name = "USER_2_ID"),
          inverseJoinColumns = @JoinColumn(name = "USER_1_ID"))
  @Builder.Default
  private Set<User> followings = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "T_BLOCKED_USERS",
          joinColumns = @JoinColumn(name = "USER_1_ID"),
          inverseJoinColumns = @JoinColumn(name = "USER_2_ID"))
  @Builder.Default
  private Set<User> blockedUsers = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  private Profile profile;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Post> posts = new HashSet<>();

  @ManyToMany(mappedBy = "participants", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @Builder.Default
  private Set<Trip> trips = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  @Builder.Default
  private Set<ChatChannelUser> userChatChannels = new HashSet<>();

  @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<FollowRequest> followRequests = new HashSet<>();

  @ManyToMany(mappedBy = "likes", cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Post> likedPosts = new HashSet<>();

  @ManyToMany(mappedBy = "likes", cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Comment> likedComments = new HashSet<>();

  @OneToMany(mappedBy = "recipient", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  @Builder.Default
  private Set<Review> reviews = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
  @Builder.Default
  private Set<UserInterest> interests = new HashSet<>();

  @Transient
  @Builder.Default
  private Integer depth = 0;

  @ManyToMany(mappedBy = "likes", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private Set<Post> favoritePosts = new LinkedHashSet<>();

  @Override
  public String getExceptionString() {
    return getUsername();
  }

  public boolean canBeSeenBy(User user) {
    return user == null || (isNotBlockedBy(user) && user.isNotBlockedBy(this));
  }

  public boolean isNotBlockedBy(@NotNull User user) {
    return !user.getBlockedUsers().contains(this);
  }

  public boolean follows(User user) {
    if (user == null)
      return false;
    return followings.contains(user);
  }

  public boolean isFollowedBy(User user) {
    if (user == null)
      return false;
    return followers.contains(user);
  }

  public boolean addFollowing(User user) {
    if (user == null)
      return false;
    return followings.add(user);
  }

  public boolean removeFollowing(User user) {
    if (user == null)
      return false;
    return followings.remove(user);
  }

  public boolean hasPendingRequestFrom(User user) {
    if (user == null)
      return false;
    return followRequests.stream().anyMatch(
            followRequest -> followRequest.getFromUser().equals(user)
    );
  }

  public void incrementDepth(Integer depth) {
    this.depth = depth + 1;
  }

  public Integer getRating() {
    return (int) reviews.stream()
            .mapToInt(Review::getRating)
            .average().orElse(0.0);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean removeFollowRequestFrom(User user) {
    if (user == null)
      return false;
    return followRequests.removeIf(followRequest -> Objects.equals(followRequest.getFromUser(), user));
  }

  public void keepInterestsAndRemoveOthers(@NotNull Set<Interest> interestSet) {
    HashSet<UserInterest> toRemove = new HashSet<>();
    interestSet.forEach(interest -> {
      UserInterest userInterest = UserInterest.of(interest, this);
      if (interests.contains(userInterest) && !interestSet.contains(interest))
        toRemove.add(userInterest);
      else if (!interests.contains(userInterest) && interestSet.contains(interest)) {
        userInterest.setIsSetFromProfile(true);
        interests.add(userInterest);
      }
    });
    interests.removeIf(toRemove::contains);
  }
  
  public UserResponse toResponse() {
    return UserResponse.fromEntity(this);
  }
  
  public UserResponse toResponse(User other) {
    return UserResponse.fromEntity(this, other);
  }
  
  public UserResponse toResponse(User other, boolean detailed) {
    return UserResponse.fromEntity(this, other, detailed);
  }

}