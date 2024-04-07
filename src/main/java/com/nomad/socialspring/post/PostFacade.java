package com.nomad.socialspring.post;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostFacade {

  private final PostRepository repository;


  public Post save(PostRequest request, Trip trip, User user, Set<Interest> interestSet, Set<Image> images) {
    Post post = Post.builder()
        .author(user)
        .content(request.content())
        .isPrivate(request.isPrivate())
        .interests(interestSet)
        .trip(trip)
        .images(images)
        .build();
    post.getImages().forEach(image -> image.setPost(post));
    return save(post);
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

  public Page<Post> findByUser(User user, User currentUser, int page, int size) {
    return repository.findByUser(user, currentUser, PageRequest.of(page, size));
  }
}
