package com.nomad.socialspring.post.service;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.model.PostMapper;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.post.repo.PostRepository;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostRepository repository;


    public Post save(PostRequest request, User user, Image image) {
        return save(PostMapper.requestToEntity(request, user, image));
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void delete(Post post) {
        repository.delete(post);
    }

    public Post findById(Long id) {
        return repository.findById(id)
                .orElseThrow(BxException.xNotFound(Post.class, id));
    }
}
