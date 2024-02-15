package com.nomad.socialspring.user;

import com.nomad.socialspring.country.CountryResponse;
import com.nomad.socialspring.image.ImageMapper;

public class ProfileMapper {
  public static ProfileResponse entityToRequest(Profile profile, boolean detailedProfile) {
    if (profile == null)
      return null;

    return ProfileResponse.builder()
            .displayName(profile.getDisplayName())
            .bio(profile.getBio())
            .gender(profile.getGender())
            .birthDate(profile.getBirthDate())
            .profileImageUrl(ImageMapper.entityToUrl(profile.getProfileImage()))
            .country(CountryResponse.fromEntity(profile.getCountry()))
            .numberOfFollowers(detailedProfile ? profile.getNumberOfFollowers() : null)
            .numberOfFollowings(detailedProfile ? profile.getNumberOfFollowings() : null)
            .build();
  }
}
