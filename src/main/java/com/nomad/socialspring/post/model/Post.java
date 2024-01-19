package com.nomad.socialspring.post.model;

import com.nomad.socialspring.comment.model.Comment;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.interest.model.Interest;
import com.nomad.socialspring.trip.model.Trip;
import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_POST")
public class Post extends BaseEntity {

    @Column(name = "CONTENT", nullable = false, length = 1200)
    @Size(max = 1200)
    private String content;

    @Column(name = "IS_PRIVATE", nullable = false)
    @Builder.Default
    private Boolean isPrivate = false;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    private User author;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "IMAGE_ID")
    private Image image;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_POST_INTERESTS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "INTEREST_ID"))
    @Builder.Default
    private Set<Interest> interests = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_POST_LIKES",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @Builder.Default
    private Set<User> likes = new LinkedHashSet<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @Override
    public String getExceptionString() {
        return getId().toString();
    }
}