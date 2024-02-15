package com.nomad.socialspring.image;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.post.Post;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_IMAGE")
public class Image extends BaseEntity {

  @Lob
  @Column(name = "DATA", length = 1000)
  private byte[] imageData;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "POST_ID")
  private Post post;

}