package com.nomad.socialspring.country.model;

import com.nomad.socialspring.country.dto.CountryResponse;

public class CountryMapper {

    public static CountryResponse entityToResponse(Country country) {
        if (country == null)
            return null;

        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }
}
