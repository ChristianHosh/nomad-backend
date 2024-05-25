package com.nomad.socialspring.interest;

import com.nomad.socialspring.error.BxException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InterestFacade {

  private final InterestRepository repository;

  public Page<Interest> getInterests(int page, int size, String name) {
    return repository.findByNameContains(name, PageRequest.of(page, size));
  }

  public Set<Interest> getInterestsFromIds(List<Long> ids) {
    if (ids == null || ids.isEmpty())
      return Set.of();

    return new HashSet<>(repository.findAllById(ids));
  }

  public Interest getInterest(Long id) {
    return repository.findById(id)
        .orElseThrow(BxException.xNotFound(Interest.class, id));
  }
}
