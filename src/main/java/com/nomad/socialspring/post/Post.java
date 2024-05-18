package com.nomad.socialspring.post;

import com.nomad.socialspring.comment.Comment;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.recommender.UserPostInteraction;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_POST")
public class Post extends BaseEntity {

  @Column(name = "CONTENT", nullable = false, length = 1200)
  @Size(max = 1200)
  private String content;

  @Column(name = "IS_PRIVATE", nullable = false)
  @Builder.Default
  private Boolean isPrivate = false;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "AUTHOR_ID", nullable = false)
  private User author;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Image> images = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "T_POST_INTERESTS", joinColumns = @JoinColumn(name = "POST_ID"), inverseJoinColumns = @JoinColumn(name = "INTEREST_ID"))
  @Builder.Default
  private Set<Interest> interests = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "T_POST_LIKES", joinColumns = @JoinColumn(name = "POST_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
  @Builder.Default
  private Set<User> likes = new HashSet<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Comment> comments = new HashSet<>();

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "TRIP_ID")
  private Trip trip;

  @Column(name = "z_score")
  private Double zScore;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "T_POST_FAVORITES",
          joinColumns = @JoinColumn(name = "POST_ID"),
          inverseJoinColumns = @JoinColumn(name = "USER_ID"))
  @Builder.Default
  private Set<User> favorites = new LinkedHashSet<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserPostInteraction> userPostInteractions = new LinkedHashSet<>();

  public boolean canBeSeenBy(User user) {
    return (!isPrivate || user.follows(author)) && (author.canBeSeenBy(user));
  }

  public boolean canBeModifiedBy(User user) {
    return user.equals(author);
  }

  public int getNumberOfLikes() {
    return likes == null ? 0 : likes.size();
  }

  public Integer getNumberOfComments() {
    return comments == null ? 0 : comments.size();
  }

  public Comment getTopComment() {
    if (comments == null || comments.isEmpty()) return null;
    return comments.stream().max(Comparator.comparingInt(Comment::getNumberOfLikes)).orElseThrow(BxException.xHardcoded("should not happen"));
  }
  
  public PostResponse toResponse() {
    return PostResponse.fromEntity(this);
  }
  
  public PostResponse toResponse(User other) {
    return PostResponse.fromEntity(this, other);
  }
  
  public Double getRecencyScore() {
    long creationTime = getCreatedOn().getTime();
    long now = System.currentTimeMillis();

    return (now - creationTime) / (1000.0 * 60 * 60);
  }
}