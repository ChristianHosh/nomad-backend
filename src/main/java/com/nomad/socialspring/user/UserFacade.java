package com.nomad.socialspring.user;

import com.nomad.socialspring.chat.ChatChannel;
import com.nomad.socialspring.location.Location;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.interest.UserInterest;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.security.AuthenticationFacade;
import com.nomad.socialspring.security.RegisterRequest;
import com.nomad.socialspring.trip.Trip;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserRepository repository;
  private final AuthenticationFacade authenticationFacade;

  public User findById(Long id) {
    return repository.findById(id)
            .orElseThrow(BxException.xNotFound(User.class, id));
  }

  public User getCurrentUser() {
    return authenticationFacade.getAuthenticatedUser();
  }

  public User getCurrentUserOrNull() {
    return authenticationFacade.getAuthenticatedUserOrNull();
  }

  public User findByUsername(String username) {
    return repository.findByUsername(username)
            .orElseThrow(BxException.xNotFound(User.class, username));
  }

  public User findByUsernameOrNull(String username) {
    return repository.findByUsername(username)
            .orElse(null);
  }

  public User findByEmailIgnoreCase(String email) {
    return repository.findByEmail(email)
            .orElseThrow(BxException.xNotFound(User.class, email));
  }

  public User save(User user) {
    return repository.save(user);
  }

  public User save(RegisterRequest registerRequest) {
    return repository.save(UserMapper.requestToEntity(registerRequest));
  }

  public boolean existsByUsernameIgnoreCase(String username) {
    return repository.existsByUsername(username);
  }

  public boolean existsByEmailIgnoreCase(String email) {
    return repository.existsByEmail(email);
  }

  public void verify(@NotNull User user) {
    user.setIsVerified(true);
    save(user);
  }

  public List<User> findByUsernameList(@NotNull List<String> usernames) {
    return repository.findByUsernameIn(usernames);
  }

  public List<User> findByIdList(@NotNull List<Long> ids) {
    return repository.findByIdIn(ids);
  }

  public Page<User> getUsersByChatChannel(ChatChannel chatChannel, int page, int size) {
    return getUsersByChatChannel(chatChannel, PageRequest.of(page, size));
  }


  public Page<User> getUsersByChatChannel(ChatChannel chatChannel, Pageable pageable) {
    return repository.findByUserChatChannels_ChatChannel(chatChannel, pageable);
  }

  public Page<User> getFollowersByUser(Long userId, int page, int size) {
    return repository.findByFollowers_Id(userId, PageRequest.of(page, size));
  }

  public Page<User> getFollowingsByUser(Long userId, int page, int size) {
    return repository.findByFollowings_Id(userId, PageRequest.of(page, size));
  }

  public Page<User> findAllByPostLiked(Post post, int page, int size) {
    return repository.findByLikedPosts_Id(post.getId(), PageRequest.of(page, size));
  }

  public Page<User> findAllByCommentLiked(Long id, int page, int size) {
    return repository.findByLikedComments_Id(id, PageRequest.of(page, size));
  }

  /**
   * @param user1 user to check followers of
   * @param user2 user to check followings of
   * @param page  page number
   * @param size  page size
   * @return a page of users that follow user1 and are followed by user2
   */
  public Page<User> getMutualFollowings(@NotNull User user1, @NotNull User user2, int page, int size) {
    List<User> mutualList = user1.getFollowers().stream()
            .filter(user2.getFollowings()::contains)
            .toList();
    return new PageImpl<>(
            mutualList.stream()
                    .skip((long) page * size)
                    .limit(size)
                    .toList(),
            PageRequest.of(page, size),
            mutualList.size()
    );
  }

  public User updateProfile(@NotNull User user, @NotNull ProfileRequest profileRequest, Set<Interest> interestSet, Location location) {
    if (profileRequest.displayName() != null)
      user.getProfile().setDisplayName(profileRequest.displayName());
    if (profileRequest.bio() != null)
      user.getProfile().setBio(profileRequest.bio());
    if (profileRequest.gender() != null)
      user.getProfile().setGender(profileRequest.gender());
    if (location != null)
      user.getProfile().setLocation(location);
    if (interestSet != null)
      user.keepInterestsAndRemoveOthers(interestSet);
    return save(user);
  }

  public List<User> getSuggestedUsers(@NotNull User currentUser) {
    List<User> suggestedUsers = new ArrayList<>(suggestUsers(currentUser));
    suggestedUsers.sort((user1, user2) -> {
      AtomicInteger sharedInterests1 = new AtomicInteger(0);
      Thread thread = new Thread(() -> sharedInterests1.set(countSharedInterests(currentUser, user1)));
      thread.start();
      int sharedInterests2 = countSharedInterests(currentUser, user2);

      try {
        thread.join();
      } catch (InterruptedException e) {
        throw BxException.unexpected(e);
      }
      if (sharedInterests1.get() == sharedInterests2)
        return Integer.compare(user1.getDepth(), user2.getDepth());
      else
        return Integer.compare(sharedInterests2, sharedInterests1.get());
    });

    return suggestedUsers;
  }

  @NotNull
  private List<User> suggestUsers(@NotNull User currentUser) {
    Set<User> potentialUsers = new HashSet<>();
    Set<User> currentUserFollowings = currentUser.getFollowings();

    Queue<User> frontier = new LinkedList<>(currentUserFollowings);
    while (!frontier.isEmpty()) {
      User user = frontier.poll();
      if (user.getDepth() < 3) {
        potentialUsers.add(user);
        user.getFollowings().forEach(userFollowing -> {
          if (userFollowing.canBeSeenBy(currentUser)) {
            userFollowing.incrementDepth(user.getDepth());
            if (userFollowing.getDepth() < 3)
              frontier.add(userFollowing);
          }
        });
      }
    }

    if (potentialUsers.size() < 10) {
      int toComplete = 25 - potentialUsers.size();
      List<User> toAdd = repository.findByRandom(currentUser, toComplete, Pageable.ofSize(toComplete));
      potentialUsers.addAll(toAdd);
    }

    return potentialUsers.stream()
            .filter(user -> !user.isFollowedBy(currentUser))
            .filter(user -> user.canBeSeenBy(currentUser))
            .limit(25)
            .toList();
  }

  private static int countSharedInterests(@NotNull User user1, @NotNull User user2) {
    Set<Interest> interests1 = user1.getInterests().stream()
            .map(UserInterest::getInterest)
            .collect(Collectors.toSet());
    Set<Interest> interests2 = user2.getInterests().stream()
            .map(UserInterest::getInterest)
            .collect(Collectors.toSet());
    interests1.retainAll(interests2);
    return interests1.size();
  }

  public Page<User> getUsersInTrip(Trip trip, int page, int size) {
    return repository.findByTrips_Id(trip.getId(), PageRequest.of(page, size));
  }

  public Page<User> getBlockedUsers(User user, int page, int size) {
    return repository.findByBlockedUsersById(user.getId(), PageRequest.of(page, size));
  }

  public Page<User> findBySearchParamExcludeBlocked(User user, String query, boolean excludeSelf, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<User> userPage = repository.findBySearchParamExcludeBlocked(query, user, excludeSelf, pageable);

    if (user != null) {
      List<User> userList = userPage.stream()
              .sorted(new UserSocialSorter(user))
              .filter(u -> u.isNotBlockedBy(user))
              .toList();

      return new PageImpl<>(userList, pageable, userPage.getTotalElements());
    }

    return userPage;
  }

  public List<User> getMentionedUsersFromContent(String content) {
    if (!content.contains("@"))
      return List.of();

    List<String> usernames = new ArrayList<>();
    int index = 0;
    while ((index = content.indexOf("@", index)) != -1) {
      String username = content.substring(index + 1, content.indexOf(" ", index));
      if (!usernames.contains(username)) {
        usernames.add(username);
      }
      index += 1;
    }

    return findByUsernameList(usernames);
  }

  private record UserSocialSorter(User currentUser) implements Comparator<User> {

    @Override
    public int compare(User user1, User user2) {
      boolean following1 = currentUser.getFollowings().contains(user1);
      boolean following2 = currentUser.getFollowings().contains(user2);

      // Users currently followed by the current user come first
      if (following1 && !following2) {
        return -1;
      } else if (!following1 && following2) {
        return 1;
      }

      // Users following the current user come next
      if (!following1 && user2.getFollowings().contains(currentUser)) {
        return -1;
      } else if (following1 && !user1.getFollowings().contains(currentUser)) {
        return 1;
      }

      // Users with no following relationship come last
      return 0;
    }
  }
}
