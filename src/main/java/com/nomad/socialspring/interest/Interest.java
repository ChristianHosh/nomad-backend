package com.nomad.socialspring.interest;

import com.nomad.socialspring.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_INTEREST")
public class Interest extends BaseEntity {

  @Column(name = "NAME", nullable = false, unique = true)
  private String name;

  public InterestResponse toResponse() {
    return new InterestResponse(this);
  }
}