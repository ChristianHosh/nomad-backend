package com.nomad.socialspring.recommender;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER_POST_INTERACTION", indexes = {
    @Index(name = "idx_userpostinteraction_postid", columnList = "POST_ID")
})
public class UserPostInteraction extends BaseEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @Enumerated
  @Column(name = "EVENT", nullable = false)
  private Event event;

  @ManyToOne
  @JoinColumn(name = "POST_ID")
  private Post post;

  @Column(name = "STRENGTH", nullable = false)
  private Integer strength;

  @Override
  public void preSave() {
    super.preSave();
    strength = event.getStrength();
  }

  @Override
  public String toString() {
    return "UserPostInteraction{" +
        "user=" + user +
        ", event=" + event +
        ", post=" + post +
        ", strength=" + strength +
        '}';
  }
}