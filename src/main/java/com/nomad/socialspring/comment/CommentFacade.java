package com.nomad.socialspring.comment;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentFacade {

    private final CommentRepository repository;


    public Comment save(CommentRequest commentRequest, User user, Post post) {
        return save(CommentMapper.requestToEntity(commentRequest, user, post));
    }

    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    public Page<Comment> findAllByPost(Post post, int page, int size) {
        return repository.findByPost(post, PageRequest.of(page, size));
    }

    public Comment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(BxException.xNotFound(Comment.class, id));
    }

    public void delete(Comment comment) {
        repository.delete(comment);
    }
}
