package com.nomad.socialspring.post.model;

import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.image.model.ImageMapper;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.user.model.UserMapper;
import com.nomad.socialspring.user.model.User;

import java.util.Set;

public class PostMapper {

    public static PostResponse entityToResponse(Post post) {
        if (post == null)
            return null;

        return PostResponse.builder()
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .id(post.getId())
                .content(post.getContent())
                .isPrivate(post.getIsPrivate())
                .images(post.getImages().stream().map(ImageMapper::entityToResponse).toList())
                .author(UserMapper.entityToResponse(post.getAuthor()))
                .numberOfLikes(post.getLikes().size())
                .build();
    }

    public static Post requestToEntity(PostRequest request, User user, Set<Interest> interestSet, Set<Image> images) {
        Post post = Post.builder()
                .author(user)
                .content(request.content())
                .isPrivate(request.isPrivate())
                .interests(interestSet)
                .images(images)
                .build();
        post.getImages().forEach(image -> image.setPost(post));
        return post;
    }
}
