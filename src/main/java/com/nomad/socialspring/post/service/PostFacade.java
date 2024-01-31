package com.nomad.socialspring.post.service;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.post.dto.PostRequest;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.post.model.PostMapper;
import com.nomad.socialspring.post.repo.PostRepository;
import com.nomad.socialspring.trip.model.Trip;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostRepository repository;


    public Post save(PostRequest request, Trip trip, User user, Set<Interest> interestSet, Set<Image> images) {
        return save(PostMapper.requestToEntity(request, trip, user, interestSet, images));
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
