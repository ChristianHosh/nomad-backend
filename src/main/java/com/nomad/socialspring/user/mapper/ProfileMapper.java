package com.nomad.socialspring.user.mapper;

import com.nomad.socialspring.user.dto.ProfileResponse;
import com.nomad.socialspring.user.model.Profile;
import com.nomad.socialspring.country.mapper.CountryMapper;

public class ProfileMapper {
    public static ProfileResponse entityToRequest(Profile profile) {
        if (profile == null)
            return null;

        return ProfileResponse.builder()
                .displayName(profile.getDisplayName())
                .bio(profile.getBio())
                .gender(profile.getGender())
                .birthDate(profile.getBirthDate())
                .profileImageUrl(profile.getProfileImage() == null ? null : profile.getProfileImage().getUrl())
                .country(CountryMapper.entityToResponse(profile.getCountry()))
                .build();
    }
}
