package com.nomad.socialspring.country;

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
@Table(name = "T_COUNTRY")
public class Country extends BaseEntity {

  @Column(name = "NAME", nullable = false, unique = true)
  private String name;

}