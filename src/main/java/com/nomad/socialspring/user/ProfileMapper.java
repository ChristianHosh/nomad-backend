package com.nomad.socialspring.user;

import com.nomad.socialspring.location.LocationResponse;
import com.nomad.socialspring.image.ImageMapper;

public class ProfileMapper {
  public static ProfileResponseR entityToRequest(Profile profile, boolean detailedProfile) {
    if (profile == null)
      return null;

    return ProfileResponseR.builder()
            .displayName(profile.getDisplayName())
            .bio(profile.getBio())
            .gender(profile.getGender())
            .birthDate(profile.getBirthDate())
            .profileImageUrl(ImageMapper.entityToUrl(profile.getProfileImage()))
            .country(LocationResponse.fromEntity(profile.getLocation()))
            .numberOfFollowers(detailedProfile ? profile.getNumberOfFollowers() : null)
            .numberOfFollowings(detailedProfile ? profile.getNumberOfFollowings() : null)
            .build();
  }
}
