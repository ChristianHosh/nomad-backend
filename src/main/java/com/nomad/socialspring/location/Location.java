package com.nomad.socialspring.location;

import com.nomad.socialspring.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_LOCATION")
public class Location extends BaseEntity {

  @Column(name = "NAME", nullable = false, unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "BELONGS_TO_ID")
  private Location belongsTo;

  public String getFullName() {
    return belongsTo == null ? name : belongsTo.getFullName() + ", " + name;
  }
}