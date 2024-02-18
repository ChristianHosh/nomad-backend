package com.nomad.socialspring.country;

import com.nomad.socialspring.common.BaseResponse;
import lombok.Getter;

@Getter
public class CountryResponse extends BaseResponse {
  private final String name;

  public CountryResponse(Country country) {
    super(country);
    name = country.getName();
  }

  public static CountryResponse fromEntity(Country country) {
    return country == null ? null : new CountryResponse(country);
  }
}
