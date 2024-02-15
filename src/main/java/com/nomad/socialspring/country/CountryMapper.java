package com.nomad.socialspring.country;

public class CountryMapper {

    public static CountryResponse entityToResponse(Country country) {
        if (country == null)
            return null;

        return new CountryResponse(country);
    }
}
