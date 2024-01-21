package com.nomad.socialspring.post.service;

import com.nomad.socialspring.comment.dto.CommentRequest;
import com.nomad.socialspring.comment.dto.CommentResponse;
import com.nomad.socialspring.comment.model.Comment;
import com.nomad.socialspring.comment.model.CommentMapper;
import com.nomad.socialspring.comment.service.CommentFacade;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.image.service.ImageFacade;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.interest.service.InterestFacade;
import com.nomad.socialspring.notification.NotificationFacade;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.post.model.PostMapper;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.model.UserMapper;
import com.nomad.socialspring.user.service.UserFacade;
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

    public PostResponse createPost(PostRequest request, List<MultipartFile> imageFiles) {
        User currentUser = userFacade.getAuthenticatedUser();

        Set<Image> images = null;
        if (imageFiles != null && !imageFiles.isEmpty())
            images = imageFacade.saveAll(imageFiles);

        Set<Interest> interestSet = interestFacade.getInterestFromTags(request.interestsTags());

        Post post = postFacade.save(request, currentUser, interestSet, images);
        return PostMapper.entityToResponse(post);
    }

    public PostResponse updatePost(Long postId, @NotNull PostRequest request) {
        User currentUser = userFacade.getAuthenticatedUser();

        Post post = postFacade.findById(postId);

        Set<Interest> interestSet = interestFacade.getInterestFromTags(request.interestsTags());
        // only author can update their posts
        if (post.canBeModifiedBy(currentUser)) {
            post.setContent(request.content());
            post.setIsPrivate(request.isPrivate());
            post.setInterests(interestSet);
            post = postFacade.save(post);
            return PostMapper.entityToResponse(post);
        }
        throw BxException.unauthorized(currentUser);
    }

    public PostResponse getPost(Long postId) {
        User currentUser = userFacade.getAuthenticatedUser();

        Post post = postFacade.findById(postId);

        // only return if post is public or current user follows post author
        if (post.canBeSeenBy(currentUser))
            return PostMapper.entityToResponse(post);
        throw BxException.unauthorized(currentUser);
    }

    public PostResponse deletePost(Long postId) {
        User currentUser = userFacade.getAuthenticatedUser();

        Post post = postFacade.findById(postId);

        if (post.canBeModifiedBy(currentUser)) {
            postFacade.delete(post);
            return PostMapper.entityToResponse(post);
        }
        throw BxException.unauthorized(currentUser);
    }

    public Page<CommentResponse> getPostComments(Long postId, int page, int size) {
        Post post = postFacade.findById(postId);

        return commentFacade.findAllByPost(post, page, size).map(CommentMapper::entityToResponse);
    }

    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        Post post = postFacade.findById(postId);
        User currentUser = userFacade.getAuthenticatedUser();

        if (post.canBeSeenBy(currentUser)) {
            Comment comment = commentFacade.save(commentRequest, currentUser, post);
            post.getComments().add(comment);
            post = postFacade.save(post);
            if (!Objects.equals(currentUser, post.getAuthor()))
                notificationFacade.notifyPostComment(post, comment);

            return CommentMapper.entityToResponse(comment);
        }

        throw BxException.unauthorized(currentUser);
    }


    public Page<UserResponse> getPostLikes(Long postId, int page, int size) {
        Post post = postFacade.findById(postId);

        return userFacade.findAllByPostLiked(post.getId(), page, size).map(UserMapper::entityToResponse);
    }

    public PostResponse likePost(Long postId) {
        Post post = postFacade.findById(postId);
        User currentUser = userFacade.getAuthenticatedUser();

        if (post.canBeSeenBy(currentUser)) {
            post.getLikes().add(currentUser);
            notificationFacade.notifyPostLike(post, currentUser);

            return PostMapper.entityToResponse(postFacade.save(post));
        }

        throw BxException.unauthorized(currentUser);
    }

    public PostResponse unlikePost(Long postId) {
        Post post = postFacade.findById(postId);
        User currentUser = userFacade.getAuthenticatedUser();

        if (post.canBeSeenBy(currentUser)) {
            post.getLikes().remove(currentUser);

            return PostMapper.entityToResponse(postFacade.save(post));
        }

        throw BxException.unauthorized(currentUser);
    }

}
