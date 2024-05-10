package com.nomad.socialspring.user;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.image.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_PROFILE")
public class Profile extends BaseEntity {

  @Column(name = "DISPLAY_NAME", nullable = false, length = 50)
  @Size(max = 50)
  private String displayName;

  @Column(name = "BIO", length = 600)
  @Size(max = 600)
  private String bio;

  @Enumerated
  @Column(name = "GENDER")
  private Gender gender;

  @Column(name = "BIRTH_DATE")
  private Date birthDate;

  @OneToOne(optional = false, orphanRemoval = true)
  @JoinColumn(name = "USER_ID", nullable = false, unique = true)
  private User user;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "PROFILE_IMAGE_ID", unique = true)
  private Image profileImage;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "LOCATION_ID")
  private Location location;

  public int getNumberOfFollowers() {
    return user.getFollowers().size();
  }

  public int getNumberOfFollowings() {
    return user.getFollowings().size();
  }

  public Integer getNumberOfPosts() {
    return user.getPosts().size();
  }
}