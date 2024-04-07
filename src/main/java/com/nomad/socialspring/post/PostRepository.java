package com.nomad.socialspring.post;

import com.nomad.socialspring.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


  @Query("""
          select p from Post p
          where
            (p.isPrivate = false) or
            (:user is not null and (:user in elements(p.author.followers) or :user = p.author))
          order by p.zScore DESC
          """)
  Page<Post> findPosts(User user, Pageable pageable);
}