package com.nomad.socialspring.interest.service;

import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.interest.repo.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InterestFacade {

    private final InterestRepository repository;

    public Set<Interest> getInterestFromTags(Set<String> interestNames) {
        if (interestNames == null || interestNames.isEmpty())
            return null;

        Set<Interest> interestList = new HashSet<>(interestNames.size());

        for (String name : interestNames) {
            Optional<Interest> interestOptional = repository.findByNameIgnoreCase(name.toLowerCase());
            Interest interest = interestOptional
                    .orElseGet(() -> repository.save(new Interest(name.toLowerCase())));
            interestList.add(interest);
        }

        return interestList;
    }
}
