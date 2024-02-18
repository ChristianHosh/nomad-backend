package com.nomad.socialspring.user;

import com.nomad.socialspring.common.BaseResponse;
import com.nomad.socialspring.country.CountryResponse;
import com.nomad.socialspring.image.ImageMapper;
import lombok.Getter;

import java.sql.Date;

@Getter
public class ProfileResponse extends BaseResponse {

  private final String displayName;
  private final String profileImageUrl;
  private Gender gender;
  private String bio;
  private CountryResponse country;
  private Date birthDate;
  private Integer numberOfFollowers;
  private Integer numberOfFollowings;


  private ProfileResponse(Profile profile, boolean detailed) {
    super(profile);
    displayName = profile.getDisplayName();
    profileImageUrl = ImageMapper.entityToUrl(profile.getProfileImage());
    
    if (detailed) {
      bio = profile.getBio();
      gender = profile.getGender();
      birthDate = profile.getBirthDate();
      numberOfFollowers = profile.getNumberOfFollowers();
      numberOfFollowings = profile.getNumberOfFollowings();
      country = CountryResponse.fromEntity(profile.getCountry());
    }
  }
  
  public static ProfileResponse fromEntity(Profile profile, boolean detailed) {
    return profile == null ? null : new ProfileResponse(profile, detailed);
  }
}
