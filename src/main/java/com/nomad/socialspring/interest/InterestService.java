package com.nomad.socialspring.interest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestService {

  private final InterestFacade interestFacade;

  public Page<InterestResponse> getInterests(int page, int size, String name) {
    return interestFacade.getInterests(page, size, name).map(InterestResponse::fromEntity);
  }
}
