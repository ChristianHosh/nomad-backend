package com.nomad.socialspring.location;

import com.nomad.socialspring.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
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
  private Set<Location> locations = new LinkedHashSet<>();

  public String getFullName() {
    return belongsTo == null ? name : name + ", " + belongsTo.getFullName();
  }

  @Override
  public void setId(Long id) {
    super.setId(id);
  }

  public LocationResponse toResponse() {
    return new LocationResponse(this);
  }

}