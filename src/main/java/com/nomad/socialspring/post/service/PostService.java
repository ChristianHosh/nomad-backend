package com.nomad.socialspring.post.service;

import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.dto.PostResponse;
import com.nomad.socialspring.post.mapper.PostMapper;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.service.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserFacade userFacade;
    private final PostFacade postFacade;

    public PostResponse createNewPost(PostRequest request, MultipartFile imageFile) {
        User currentUser = userFacade.getAuthenticatedUser();

        Image image = null;

        if (imageFile != null) {
            // upload image


        }

        // get image
        // if image is present then upload to cloud
        // get image back here to save it in the post

        Post post = postFacade.save(request, currentUser, image);

        return PostMapper.entityToResponse(post);
    }
}
