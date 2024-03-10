package com.nomad.socialspring.location;

import com.nomad.socialspring.common.BaseResponse;
import lombok.Getter;

@Getter
public class LocationResponse extends BaseResponse {
  private final String name;

  public LocationResponse(Location location) {
    super(location);
    name = location.getFullName();
  }

  public static LocationResponse fromEntity(Location location) {
    return location == null ? null : new LocationResponse(location);
  }
}
