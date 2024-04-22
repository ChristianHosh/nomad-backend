package com.nomad.socialspring.search;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.InterestRepository;
import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.location.LocationRepository;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

  private final UserFacade userFacade;
  private final LocationRepository locationRepository;
  private final InterestRepository interestRepository;

  @Transactional
  public SearchResult search(String query, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    SearchResult result;
    try {
      Future<Page<User>> userPageFuture = userFacade.searchUsersAsync(query, pageable);
      Future<Page<Interest>> interestPageFuture = interestRepository.searchInterestsAsync(query, pageable);
      Future<Page<Location>> locationPageFuture = locationRepository.searchLocationsAsync(query, pageable);

      result = new SearchResult(
              userPageFuture.get().map(User::toResponse),
              locationPageFuture.get().map(Location::toResponse),
              interestPageFuture.get().map(Interest::toResponse)
      );
    } catch (InterruptedException | ExecutionException e) {
      log.error("search error", e);
      throw BxException.unexpected(e);
    }
    return result;
  }
}
