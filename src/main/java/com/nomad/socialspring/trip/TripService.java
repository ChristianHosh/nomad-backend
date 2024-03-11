package com.nomad.socialspring.trip;


import com.nomad.socialspring.chat.ChatChannel;
import com.nomad.socialspring.chat.ChatChannelFacade;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {

  private final TripFacade tripFacade;
  private final UserFacade userFacade;
  private final ChatChannelFacade chatChannelFacade;
  private final NotificationFacade notificationFacade;

  public Page<UserResponse> getUsersInTrip(Long tripId, int page, int size) {
    Trip trip = tripFacade.findById(tripId);
    User currentUser = userFacade.getCurrentUserOrNull();

    return userFacade
            .getUsersInTrip(trip, page, size)
            .map(u -> u.toResponse(currentUser));
  }

  @Transactional
  public Object joinTrip(Long tripId) {
    Trip trip = tripFacade.findById(tripId);
    User currentUser = userFacade.getCurrentUser();
    if (!trip.addParticipant(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_TRIP, currentUser);

    ChatChannel chatChannel = chatChannelFacade.findByTrip(trip);
    if (!chatChannel.addUser(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_CHANNEL, currentUser);

    notificationFacade.notifyTripJoin(trip, currentUser);
    return trip; //todo response
  }

  @Transactional
  public Object leaveTrip(Long tripId) {
    Trip trip = tripFacade.findById(tripId);
    User currentUser = userFacade.getCurrentUser();
    if (!trip.removeParticipant(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_USER_FROM_TRIP, currentUser);

    ChatChannel chatChannel = chatChannelFacade.findByTrip(trip);
    if (!chatChannel.removeUser(currentUser))
      throw BxException.hardcoded(BxException.X_COULD_NOT_REMOVE_USER_FROM_CHANNEL, currentUser);

    return trip; //todo response
  }
}
