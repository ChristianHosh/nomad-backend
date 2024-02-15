package com.nomad.socialspring.post;

import com.nomad.socialspring.comment.Comment;
import com.nomad.socialspring.common.BDate;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.Image;
import com.nomad.socialspring.interest.Interest;
import com.nomad.socialspring.trip.Trip;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_POST")
public class Post extends BaseEntity {

    private static final float LIKE_MULTIPLIER = 1f;
    private static final float COMMENT_MULTIPLIER = 1.5f;
    private static final float RECENT_MULTIPLIER = 0.25f;


    @Column(name = "CONTENT", nullable = false, length = 1200)
    @Size(max = 1200)
    private String content;

    @Column(name = "IS_PRIVATE", nullable = false)
    @Builder.Default
    private Boolean isPrivate = false;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Image> images = new LinkedHashSet<>();

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

    public boolean canBeSeenBy(User user) {
        return (!isPrivate || user.follows(author)) && (author.canBeSeenBy(user));
    }

    public boolean canBeModifiedBy(User user) {
        return user.equals(author);
    }

    public int getNumberOfLikes() {
        return likes == null ? 0 : likes.size();
    }

    public Comment getTopComment() {
        Set<Comment> comments = getComments();
        if (comments == null || comments.isEmpty())
            return null;
        return comments.stream()
                .max(Comparator.comparingInt(Comment::getNumberOfLikes))
                .orElseThrow(BxException.xHardcoded("should not happen"));
    }

    public int getGlobalScore() {
        float recentScore = (BDate.valueOf(getCreatedOn()).differenceInMinutes(BDate.currentDate()) * RECENT_MULTIPLIER);
        return (int) (comments.size() * COMMENT_MULTIPLIER +
                        likes.size() * LIKE_MULTIPLIER +
                        recentScore);
    }
}