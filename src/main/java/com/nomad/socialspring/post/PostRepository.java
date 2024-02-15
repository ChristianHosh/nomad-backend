package com.nomad.socialspring.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
                select p from Post p inner join p.author.followers af
                where (
                    p.isPrivate = false or (:id is not null and af.id = :id)
                )
            """)
    Page<Post> findGlobalPosts(@Param("id") Long id, Pageable pageable);

    @Query("""
                select count(p) from Post p inner join p.author.followers af
                where (
                    p.isPrivate = false or (:id is not null and af.id = :id)
                )
            """)
    long countGlobal(@Param("id") Long id);
}