package com.nomad.socialspring.post.service;

import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.service.ChatChannelFacade;
import com.nomad.socialspring.comment.dto.CommentRequest;
import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.comment.model.Comment;
import com.nomad.socialspring.comment.model.CommentMapper;
import com.nomad.socialspring.comment.service.CommentFacade;
import com.nomad.socialspring.country.service.CountryFacade;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.image.service.ImageFacade;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.interest.service.InterestFacade;
import com.nomad.socialspring.notification.service.NotificationFacade;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.post.model.PostMapper;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.trip.model.Trip;
import com.nomad.socialspring.trip.service.TripFacade;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.model.UserMapper;
import com.nomad.socialspring.user.service.UserFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserFacade userFacade;
    private final PostFacade postFacade;
    private final ImageFacade imageFacade;
    private final InterestFacade interestFacade;
    private final CommentFacade commentFacade;
    private final NotificationFacade notificationFacade;
    private final CountryFacade countryFacade;
    private final TripFacade tripFacade;
    private final ChatChannelFacade chatChannelFacade;

    @Transactional
    public PostResponse createPost(@NotNull PostRequest request, List<MultipartFile> imageFiles) {
        User currentUser = userFacade.getCurrentUser();

        Set<Image> images = imageFacade.saveAll(imageFiles);
        Trip trip = null;
        if (request.trip() != null) {
            trip = tripFacade.save(request.trip(), countryFacade.findById(request.trip().countryId()));

            ChatChannel chatChannel = ChatChannel.builder()
                    .trip(trip)
                    .build();
            if (!chatChannel.addUser(currentUser))
                throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_CHANNEL, currentUser);
            chatChannelFacade.save(chatChannel);

            if (!trip.addParticipant(currentUser))
                throw BxException.hardcoded(BxException.X_COULD_NOT_ADD_USER_TO_TRIP, currentUser);
        }

        Set<Interest> interestSet = interestFacade.getInterestFromTags(request.interestsTags());

        Post post = postFacade.save(request, trip, currentUser, interestSet, images);
        return PostMapper.entityToResponse(post, currentUser);
    }

    @Transactional
    public PostResponse updatePost(Long postId, @NotNull PostRequest request) {
        User currentUser = userFacade.getCurrentUser();
        Post post = postFacade.findById(postId);

        Set<Interest> interestSet = interestFacade.getInterestFromTags(request.interestsTags());
        // only author can update their posts
        if (post.canBeModifiedBy(currentUser)) {
            post.setContent(request.content());
            post.setIsPrivate(request.isPrivate());
            post.setInterests(interestSet);
            post = postFacade.save(post);
            return PostMapper.entityToResponse(post, currentUser);
        }
        throw BxException.unauthorized(currentUser);
    }

    public PostResponse getPost(Long postId) {
        User currentUser = userFacade.getCurrentUserOrNull();
        Post post = postFacade.findById(postId);

        // only return if post is public or current user follows post author
        if (post.canBeSeenBy(currentUser))
            return PostMapper.entityToResponse(post, currentUser);
        throw BxException.unauthorized(currentUser);
    }

    @Transactional
    public PostResponse deletePost(Long postId) {
        User currentUser = userFacade.getCurrentUser();
        Post post = postFacade.findById(postId);

        if (post.canBeModifiedBy(currentUser)) {
            postFacade.delete(post);
            return PostMapper.entityToResponse(post);
        }
        throw BxException.unauthorized(currentUser);
    }

    public Page<CommentResponse> getPostComments(Long postId, int page, int size) {
        User currentUser = userFacade.getCurrentUserOrNull();
        Post post = postFacade.findById(postId);

        if (post.canBeSeenBy(currentUser))
            return commentFacade
                .findAllByPost(post, page, size)
                .map(c -> CommentMapper.entityToResponse(c, currentUser));
        throw BxException.unauthorized(currentUser);
    }

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        User currentUser = userFacade.getCurrentUser();
        Post post = postFacade.findById(postId);

        if (post.canBeSeenBy(currentUser)) {
            Comment comment = commentFacade.save(commentRequest, currentUser, post);
            post.getComments().add(comment);
            post = postFacade.save(post);
            if (!Objects.equals(currentUser, post.getAuthor()))
                notificationFacade.notifyPostComment(post, comment);

            return CommentMapper.entityToResponse(comment, currentUser);
        }
        throw BxException.unauthorized(currentUser);
    }

    public Page<UserResponse> getPostLikes(Long postId, int page, int size) {
        Post post = postFacade.findById(postId);
        User currentUser = userFacade.getCurrentUserOrNull();

        if (post.canBeSeenBy(currentUser))
            return userFacade
                    .findAllByPostLiked(post, page, size)
                    .map(u -> UserMapper.entityToResponse(u, currentUser));
        throw BxException.unauthorized(currentUser);
    }

    @Transactional
    public PostResponse likePost(Long postId) {
        Post post = postFacade.findById(postId);
        User currentUser = userFacade.getCurrentUser();

        if (post.canBeSeenBy(currentUser)) {
            if (!post.getLikes().add(currentUser))
                throw BxException.hardcoded(BxException.X_COULD_NOT_LIKE_POST, currentUser);

            notificationFacade.notifyPostLike(post, currentUser);

            return PostMapper.entityToResponse(postFacade.save(post), currentUser);
        }
        throw BxException.unauthorized(currentUser);
    }

    @Transactional
    public PostResponse unlikePost(Long postId) {
        Post post = postFacade.findById(postId);
        User currentUser = userFacade.getCurrentUser();

        if (post.canBeSeenBy(currentUser)) {
            if (!post.getLikes().remove(currentUser))
                throw BxException.hardcoded(BxException.X_COULD_NOT_UNLIKE_POST, currentUser);

            return PostMapper.entityToResponse(postFacade.save(post), currentUser);
        }
        throw BxException.unauthorized(currentUser);
    }

    public Page<PostResponse> getGlobalPosts(int page, int size) {
        User currentUser = userFacade.getCurrentUserOrNull();

        return postFacade
                .getGlobalPosts(currentUser, page, size)
                .map(post -> PostMapper.entityToResponse(post, currentUser));
    }
}
