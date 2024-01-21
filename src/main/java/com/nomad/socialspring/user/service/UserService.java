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
        User currentUser = userFacade.getAuthenticatedUserOrNull();
        User otherUser = userFacade.findById(userId);

        return UserMapper.entityToResponse(otherUser, currentUser);
    }

    @Transactional
    public UserResponse followUser(Long userId) {
        User currentUser = userFacade.getAuthenticatedUser();
        User otherUser = userFacade.findById(userId);

        if (currentUser.follows(otherUser))
            throw BxException.badRequest(User.class, BxException.X_CURRENT_USER_ALREADY_FOLLOWS);

        FollowRequest followRequest = followRequestFacade.save(new FollowRequest(currentUser, otherUser));
        notificationFacade.notifyFollowRequest(followRequest);

        return UserMapper.entityToResponse(otherUser);
    }

    public UserResponse unfollowUser(Long userId) {
        User currentUser = userFacade.getAuthenticatedUser();
        User otherUser = userFacade.findById(userId);

        if (!currentUser.follows(otherUser))
            throw BxException.badRequest(User.class, BxException.X_CURRENT_USER_ALREADY_UNFOLLOWS);

        otherUser.removeFollowing(currentUser);
        return UserMapper.entityToResponse(userFacade.save(otherUser), currentUser);
    }


    public Page<FollowRequestResponse> getUserFollowRequests(int page, int size) {
        User currentUser = userFacade.getAuthenticatedUser();

        return followRequestFacade.findByUser(currentUser, page, size).map(FollowRequestMapper::entityToResponse);
    }

    @Transactional
    public UserResponse acceptFollowRequest(Long followRequestId) {
        User currentUser = userFacade.getAuthenticatedUser();
        FollowRequest followRequest = followRequestFacade.findById(followRequestId);

        if (followRequest.isForUser(currentUser)) {
            followRequest.getFromUser().addFollowing(currentUser);
            followRequestFacade.delete(followRequest);
        }

        return UserMapper.entityToResponse(userFacade.save(followRequest.getFromUser()));
    }

    @Transactional
    public UserResponse declineFollowRequest(Long followRequestId) {
        User currentUser = userFacade.getAuthenticatedUser();
        FollowRequest followRequest = followRequestFacade.findById(followRequestId);

        if (followRequest.isForUser(currentUser)) {
            followRequestFacade.delete(followRequest);
        }

        return UserMapper.entityToResponse(followRequest.getFromUser());
    }

    public Page<UserResponse> getUserFollowers(Long userId, int page, int size) {
        User user = userFacade.findById(userId);

        return userFacade.getFollowersByUser(user.getId(), page, size).map(UserMapper::entityToResponse);
    }

    public Page<UserResponse> getUserFollowings(Long userId, int page, int size) {
        User user = userFacade.findById(userId);

        return userFacade.getFollowingsByUser(user.getId(), page, size).map(UserMapper::entityToResponse);
    }

    public UserResponse updateProfileInfo(ProfileRequest profileRequest) {
        User currentUser = userFacade.getAuthenticatedUser();

        Set<Interest> interestSet = interestFacade.getInterestFromTags(profileRequest.interestsTags());

        Country country = null;
        if (profileRequest.countryId() != null)
            country = countryFacade.findById(profileRequest.countryId());

        currentUser.getProfile().setBio(profileRequest.bio());
        currentUser.getProfile().setDisplayName(profileRequest.displayName());
        currentUser.getProfile().setGender(profileRequest.gender());
        currentUser.getProfile().setInterests(interestSet);
        currentUser.getProfile().setCountry(country);

        return UserMapper.entityToResponse(userFacade.save(currentUser));
    }

    public UserResponse updateProfileImage(MultipartFile imageFile) {
        User currentUser = userFacade.getAuthenticatedUser();

        currentUser.getProfile().setProfileImage(imageFacade.save(imageFile));

        return UserMapper.entityToResponse(userFacade.save(currentUser));
    }

    public UserResponse deleteProfileImage() {
        User currentUser = userFacade.getAuthenticatedUser();

        currentUser.getProfile().setProfileImage(null);

        return UserMapper.entityToResponse(userFacade.save(currentUser));
    }
}
