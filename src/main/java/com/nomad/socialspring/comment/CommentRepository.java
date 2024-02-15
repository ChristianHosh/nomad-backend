package com.nomad.socialspring.comment;

import com.nomad.socialspring.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.post = :post order by c.createdOn desc ")
    Page<Comment> findByPost(@Param("post") Post post, Pageable pageable);
}