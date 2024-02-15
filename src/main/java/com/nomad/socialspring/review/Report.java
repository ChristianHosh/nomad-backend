package com.nomad.socialspring.review;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_REPORT")
public class Report extends BaseEntity {

  @Column(name = "CONTENT", nullable = false, length = 500)
  @Size(max = 500)
  private String content;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "AUTHOR_ID")
  private User author;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "RECIPIENT_ID")
  private User recipient;

}