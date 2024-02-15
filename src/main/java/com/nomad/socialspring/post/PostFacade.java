package com.nomad.socialspring.post;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

    public Page<Post> getGlobalPosts(User user, int page, int size) {
        return new PageImpl<>(repository
                .findGlobalPosts(user == null ? null : user.getId(), PageRequest.of(page, size))
                .stream().sorted(Comparator.comparingInt(Post::getGlobalScore))
                .toList(),
                PageRequest.of(page, size),
                repository.countGlobal(user == null ? null : user.getId()));
    }
}
