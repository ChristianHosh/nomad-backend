package com.nomad.socialspring.country.dto;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.country.model.Country;
import lombok.Getter;

@Getter
public class CountryResponse extends BaseResponse {
  private final String name;
  public CountryResponse(Country country) {
    super(country);
    this.name = country.getName();
  }
  
  public static CountryResponse fromEntity(Country country) {
    return country == null ? null : new CountryResponse(country);
  }
}
