package com.nomad.socialspring.user.model;

import com.nomad.socialspring.image.model.ImageMapper;
import com.nomad.socialspring.user.dto.ProfileResponse;
import com.nomad.socialspring.country.model.CountryMapper;

public class ProfileMapper {
    public static ProfileResponse entityToRequest(Profile profile) {
        if (profile == null)
            return null;

        return ProfileResponse.builder()
                .displayName(profile.getDisplayName())
                .bio(profile.getBio())
                .gender(profile.getGender())
                .birthDate(profile.getBirthDate())
                .profileImageUrl(ImageMapper.entityToUrl(profile.getProfileImage()))
                .country(CountryMapper.entityToResponse(profile.getCountry()))
                .build();
    }
}
