package com.nomad.socialspring.user.service;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.country.model.Country;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.security.dto.RegisterRequest;
import com.nomad.socialspring.security.facade.AuthenticationFacade;
import com.nomad.socialspring.user.dto.ProfileRequest;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.model.UserMapper;
import com.nomad.socialspring.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<User> userList = new ArrayList<>(usernames.size());

        usernames.forEach(username -> {
            User user = findByUsernameOrNull(username);
            if (user != null)
                userList.add(user);
        });

        return userList;
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

    public Page<User> findAllByPostLiked(Long postId, int page, int size) {
        return repository.findByLikedPosts_Id(postId, PageRequest.of(page, size));
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

    public User updateProfile(@NotNull User user, @NotNull ProfileRequest profileRequest, Set<Interest> interestSet, Country country) {
        user.getProfile().setBio(profileRequest.bio());
        user.getProfile().setDisplayName(profileRequest.displayName());
        user.getProfile().setGender(profileRequest.gender());
        user.getProfile().setInterests(interestSet);
        user.getProfile().setCountry(country);
        return save(user);
    }

    public List<User> getSuggestedUsers(@NotNull User currentUser) {
        List<User> suggestedUsers = new ArrayList<>(suggestUsers(currentUser));
        suggestedUsers.sort((user1, user2) -> {
            int sharedInterests1 = countSharedInterests(currentUser, user1);
            int sharedInterests2 = countSharedInterests(currentUser, user2);
            if (sharedInterests1 == sharedInterests2)
                return Integer.compare(user1.getDepth(), user2.getDepth());
            else
                return Integer.compare(sharedInterests2, sharedInterests1);
        });

        return suggestedUsers;
    }

    @NotNull
    private static List<User> suggestUsers(@NotNull User currentUser) {
        Set<User> potentialUsers = new HashSet<>();
        Set<User> currentUserFollowings = currentUser.getFollowings();

        Queue<User> frontier = new LinkedList<>(currentUserFollowings);
        while (!frontier.isEmpty()) {
            User user = frontier.poll();
            if (user.getDepth() < 3) {
                potentialUsers.add(user);
                user.getFollowings().forEach(userFollowing -> {
                    userFollowing.incrementDepth(user.getDepth());
                    if (userFollowing.getDepth() < 3)
                        frontier.add(userFollowing);
                });
            }
        }
        return potentialUsers.stream().filter(user -> !user.isFollowedBy(currentUser)).limit(25).toList();
    }

    private static int countSharedInterests(@NotNull User user1, @NotNull User user2) {
        Set<Interest> interests1 = user1.getProfile().getInterests();
        Set<Interest> interests2 = user2.getProfile().getInterests();
        return (int) interests1.stream().filter(interests2::contains).count();
    }
}
