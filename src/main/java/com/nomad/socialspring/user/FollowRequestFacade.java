package com.nomad.socialspring.user;

import com.nomad.socialspring.error.BxException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowRequestFacade {

  private final FollowRequestRepository repository;

  public FollowRequest save(FollowRequest followRequest) {
    return repository.save(followRequest);
  }

  public FollowRequest findById(Long id) {
    return repository.findById(id)
        .orElseThrow(BxException.xNotFound(FollowRequest.class, id));
  }

  public void delete(FollowRequest followRequest) {
    repository.delete(followRequest);
  }

  public Page<FollowRequest> findByUser(User user, int page, int size) {
    return repository.findByToUser(user, PageRequest.of(page, size));
  }
}
