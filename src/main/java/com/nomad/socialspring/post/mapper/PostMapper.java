package com.nomad.socialspring.post.mapper;

import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.user.mapper.UserMapper;
import com.nomad.socialspring.user.model.User;

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
                .imageUrl(post.getImage() == null ? null : post.getImage().getUrl())
                .author(UserMapper.entityToResponse(post.getAuthor()))
                .numberOfLikes(post.getLikes().size())
                .build();
    }

    public static Post requestToEntity(PostRequest request, User user) {
        return requestToEntity(request, user, null);
    }

    public static Post requestToEntity(PostRequest request, User user, Image image) {
        return Post.builder()
                .author(user)
                .content(request.content())
                .isPrivate(request.isPrivate())
                .build();
    }
}
