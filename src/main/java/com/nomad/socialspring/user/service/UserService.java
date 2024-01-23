package com.nomad.socialspring.user.service;

import com.nomad.socialspring.country.model.Country;
import com.nomad.socialspring.country.service.CountryFacade;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.image.service.ImageFacade;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.interest.service.InterestFacade;
import com.nomad.socialspring.notification.service.NotificationFacade;
import com.nomad.socialspring.user.dto.FollowRequestResponse;
import com.nomad.socialspring.user.dto.ProfileRequest;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.model.FollowRequest;
import com.nomad.socialspring.user.model.FollowRequestMapper;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.model.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final FollowRequestFacade followRequestFacade;
    private final InterestFacade interestFacade;
    private final ImageFacade imageFacade;
    private final CountryFacade countryFacade;
    private final NotificationFacade notificationFacade;

    public UserResponse getUser(Long userId) {
        User user = userFacade.findById(userId);

        return UserMapper.entityToResponse(user, userFacade.getCurrentUserOrNull());
    }

    @Transactional
    public UserResponse followUser(Long userId) {
        User user = userFacade.findById(userId);
        User currentUser = userFacade.getCurrentUser();

        if (currentUser.follows(user))
            throw BxException.badRequest(User.class, BxException.X_CURRENT_USER_ALREADY_FOLLOWS);

        FollowRequest followRequest = followRequestFacade.save(new FollowRequest(currentUser, user));
        notificationFacade.notifyFollowRequest(followRequest);

        return UserMapper.entityToResponse(user);
    }

    public UserResponse unfollowUser(Long userId) {
        User user = userFacade.findById(userId);
        User currentUser = userFacade.getCurrentUser();

        if (!currentUser.follows(user))
            throw BxException.badRequest(User.class, BxException.X_CURRENT_USER_ALREADY_UNFOLLOWS);

        user.removeFollowing(currentUser);
        return UserMapper.entityToResponse(userFacade.save(user), currentUser);
    }


    public Page<FollowRequestResponse> getUserFollowRequests(int page, int size) {
        User currentUser = userFacade.getCurrentUser();

        return followRequestFacade.findByUser(currentUser, page, size).map(FollowRequestMapper::entityToResponse);
    }

    @Transactional
    public UserResponse acceptFollowRequest(Long followRequestId) {
        FollowRequest followRequest = followRequestFacade.findById(followRequestId);
        User currentUser = userFacade.getCurrentUser();

        if (followRequest.isForUser(currentUser)) {
            followRequest.getFromUser().addFollowing(currentUser);
            followRequestFacade.delete(followRequest);
        }

        return UserMapper.entityToResponse(userFacade.save(followRequest.getFromUser()));
    }

    @Transactional
    public UserResponse declineFollowRequest(Long followRequestId) {
        FollowRequest followRequest = followRequestFacade.findById(followRequestId);
        User currentUser = userFacade.getCurrentUser();

        if (followRequest.isForUser(currentUser)) {
            followRequestFacade.delete(followRequest);
        }

        return UserMapper.entityToResponse(followRequest.getFromUser());
    }

    public Page<UserResponse> getUserFollowers(Long userId, int page, int size) {
        User user = userFacade.findById(userId);

        return userFacade
                .getFollowersByUser(user.getId(), page, size)
                .map(u -> UserMapper.entityToResponse(u, userFacade.getCurrentUserOrNull()));
    }

    public Page<UserResponse> getUserFollowings(Long userId, int page, int size) {
        User user = userFacade.findById(userId);

        return userFacade
                .getFollowingsByUser(user.getId(), page, size)
                .map(u -> UserMapper.entityToResponse(u, userFacade.getCurrentUserOrNull()));
    }

    public UserResponse updateProfileInfo(@NotNull ProfileRequest profileRequest) {
        User currentUser = userFacade.getCurrentUser();

        Set<Interest> interestSet = interestFacade.getInterestFromTags(profileRequest.interestsTags());

        Country country = null;
        if (profileRequest.countryId() != null)
            country = countryFacade.findById(profileRequest.countryId());

        currentUser = userFacade.updateProfile(currentUser, profileRequest, interestSet, country);

        return UserMapper.entityToResponse(currentUser);
    }

    public UserResponse updateProfileImage(MultipartFile imageFile) {
        User currentUser = userFacade.getCurrentUser();

        currentUser.getProfile().setProfileImage(imageFacade.save(imageFile));

        return UserMapper.entityToResponse(userFacade.save(currentUser));
    }

    public UserResponse deleteProfileImage() {
        User currentUser = userFacade.getCurrentUser();

        currentUser.getProfile().setProfileImage(null);

        return UserMapper.entityToResponse(userFacade.save(currentUser));
    }

    public Page<UserResponse> getUserMutualFollowings(Long userId, int page, int size) {
        User user = userFacade.findById(userId);
        User currentUser = userFacade.getCurrentUser();

        return userFacade
                .getMutualFollowings(user, currentUser, page, size)
                .map(u -> UserMapper.entityToResponse(u, currentUser));
    }
}
