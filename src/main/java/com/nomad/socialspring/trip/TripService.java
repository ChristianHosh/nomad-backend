package com.nomad.socialspring.trip;


import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.post.PostFacade;
import com.nomad.socialspring.post.PostResponse;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import com.nomad.socialspring.user.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {

  private final TripFacade tripFacade;
  private final UserFacade userFacade;
  private final NotificationFacade notificationFacade;
  private final PostFacade postFacade;

  public Page<UserResponse> getUsersInTrip(Long tripId, int page, int size) {
    Trip trip = tripFacade.findById(tripId);
    User currentUser = userFacade.getCurrentUserOrNull();

    return userFacade
        .getUsersInTrip(trip, page, size)
        .map(u -> u.toResponse(currentUser));
  }

  @Transactional
  public TripResponse joinTrip(Long tripId) {
    Trip trip = tripFacade.findById(tripId);
    User currentUser = userFacade.getCurrentUser();
    if (!trip.addParticipant(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_TRIP, currentUser);

    notificationFacade.notifyTripJoin(trip, currentUser);
    return trip.toResponse(currentUser);
  }

  @Transactional
  public TripResponse leaveTrip(Long tripId) {
    Trip trip = tripFacade.findById(tripId);
    User currentUser = userFacade.getCurrentUser();
    if (!trip.removeParticipant(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_USER_FROM_TRIP, currentUser);

    return trip.toResponse(currentUser);
  }

  public Page<PostResponse> getMyTrips(int page, int size) {
    User currentUser = userFacade.getCurrentUser();
    return postFacade.getMyTrips(currentUser, page, size)
        .map(p -> p.toResponse(currentUser, true));
  }

  @Transactional
  public TripResponse updateTripStatusAsWent(Long tripId) {
    return updateTripUserStatus(tripId, TripUser.TripUserStatus.WENT);
  }

  @Transactional
  protected TripResponse updateTripUserStatus(Long tripId, TripUser.TripUserStatus status) {
    User currentUser = userFacade.getCurrentUser();
    Trip trip = tripFacade.findById(tripId);

    TripUser tripUser = trip.findTripUser(currentUser);
    if (tripUser == null)
      throw BxException.hardcoded(BxException.X_USER_NOT_IN_TRIP, currentUser);
    if (!tripUser.getStatus().equals(TripUser.TripUserStatus.JOINED))
      throw BxException.hardcoded(BxException.X_CANT_UPDATE_TRIP_USER_STATUS, currentUser);

    tripUser.setStatus(status);
    return tripFacade.save(trip).toResponse(currentUser);
  }

  @Transactional
  public TripResponse updateTripStatusAsNotWent(Long tripId) {
    return updateTripUserStatus(tripId, TripUser.TripUserStatus.DIDNT_GO);
  }

  public Page<PostResponse> getUpcomingTrips(int page, int size) {
    User currentUser = userFacade.getCurrentUser();
    return postFacade.getUpcomingTrips(currentUser, page, size)
        .map(p -> p.toResponse(currentUser));
  }
}
