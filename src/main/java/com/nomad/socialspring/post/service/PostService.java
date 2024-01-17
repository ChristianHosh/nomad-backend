package com.nomad.socialspring.post.service;

import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.image.service.ImageFacade;
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
    private final ImageFacade imageFacade;

    public PostResponse createNewPost(PostRequest request, MultipartFile imageFile) {
        User currentUser = userFacade.getAuthenticatedUser();

        Image image = null;
        if (imageFile != null)
            image = imageFacade.save(imageFile);

        Post post = postFacade.save(request, currentUser, image);

        return PostMapper.entityToResponse(post);
    }
}
