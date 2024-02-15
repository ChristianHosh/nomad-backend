package com.nomad.socialspring.interest;

public class InterestMapper {

  public static InterestResponse entityToResponse(Interest interest) {
    if (interest == null)
      return null;

    return new InterestResponse(interest);
  }
}
