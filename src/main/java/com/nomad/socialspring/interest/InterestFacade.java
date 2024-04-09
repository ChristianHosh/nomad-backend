package com.nomad.socialspring.interest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InterestFacade {

  private final InterestRepository repository;

  public Set<Interest> getInterestFromTags(Set<String> interestNames) {
    if (interestNames == null || interestNames.isEmpty())
      return Set.of();

    Set<Interest> interestList = new HashSet<>(interestNames.size());

    for (String name : interestNames) {
      Optional<Interest> interestOptional = repository.findByNameIgnoreCase(name.toLowerCase());
      Interest interest = interestOptional
              .orElseGet(() -> repository.save(new Interest(name.toLowerCase())));
      interestList.add(interest);
    }

    return interestList;
  }

  public Page<Interest> getInterests(int page, int size, String name) {
    return repository.findByNameContains(name, PageRequest.of(page, size));
  }

  public Set<Interest> getInterestsFromIds(List<Long> ids) {
    if (ids == null || ids.isEmpty())
      return Set.of();
    
    return new HashSet<>(repository.findAllById(ids));
  }
}
