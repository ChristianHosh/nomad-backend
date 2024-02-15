package com.nomad.socialspring.interest;

import com.nomad.socialspring.common.BaseResponse;
import lombok.Getter;

/**
 * Response DTO for {@link Interest}
 */
@Getter
public class InterestResponse extends BaseResponse {
  private final String name;
  public InterestResponse(Interest interest) {
    super(interest);
    this.name = interest.getName();
  }
  
  public static InterestResponse fromEntity(Interest interest) {
    return interest == null ? null : new InterestResponse(interest);
  }
}