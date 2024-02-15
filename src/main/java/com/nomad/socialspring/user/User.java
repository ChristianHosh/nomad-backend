package com.nomad.socialspring.user;

import com.nomad.socialspring.chat.ChatChannelUser;
import com.nomad.socialspring.comment.Comment;
import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.review.Review;
import com.nomad.socialspring.trip.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER")
public class User extends BaseEntity {

    @Column(name = "USERNAME", nullable = false, unique = true, length = 30)
    @Size(min = 4, max = 30)
    private String username;

    @Column(name = "PASSWORD", nullable = false, unique = true)
    private String password;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 50)
    @Size(min = 4, max = 50)
    @Email
    private String email;

    @Column(name = "IS_VERIFIED", nullable = false)
    @Builder.Default
    private Boolean isVerified = false;

    @Enumerated
    @Column(name = "ROLE", nullable = false)
    private Role role;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_USER_FOLLOWERS",
            joinColumns = @JoinColumn(name = "USER_1_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_2_ID"))
    @Builder.Default
    private Set<User> followers = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_USER_FOLLOWERS",
            joinColumns = @JoinColumn(name = "USER_2_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_1_ID"))
    @Builder.Default
    private Set<User> followings = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_BLOCKED_USERS",
            joinColumns = @JoinColumn(name = "USER_1_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_2_ID"))
    @Builder.Default
    private Set<User> blockedUsers = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Post> posts = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "participants", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @Builder.Default
    private Set<Trip> trips = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @Builder.Default
    private Set<ChatChannelUser> userChatChannels = new LinkedHashSet<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<FollowRequest> followRequests = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "likes", cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Post> likedPosts = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "likes", cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Comment> likedComments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "recipient", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @Builder.Default
    private Set<Review> reviews = new LinkedHashSet<>();

    @Transient
    @Builder.Default
    private Integer depth = 0;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) object;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public boolean canBeSeenBy(User user) {
        return user == null || (isNotBlockedBy(user) && user.isNotBlockedBy(this));
    }

    public boolean isNotBlockedBy(@NotNull User user) {
        return !user.getBlockedUsers().contains(this);
    }

    public boolean follows(User user) {
        if (user == null)
            return false;
        return followings.contains(user);
    }

    public boolean isFollowedBy(User user) {
        if (user == null)
            return false;
        return followers.contains(user);
    }

    public boolean addFollowing(User user) {
        if (user == null)
            return false;
        return followings.add(user);
    }


    public boolean removeFollowing(User user) {
        if (user == null)
            return false;
        return followings.remove(user);
    }

    public boolean hasPendingRequestFrom(User user) {
        if (user == null)
            return false;
        return followRequests.stream().anyMatch(
                followRequest -> followRequest.getFromUser().equals(user)
        );
    }

    @Override
    public String getExceptionString() {
        return getUsername();
    }

    public void incrementDepth(Integer depth) {
        this.depth = depth + 1;
    }

    public Integer getRating() {
        return (int) reviews.stream()
                .mapToInt(Review::getRating)
                .average().orElse(0.0);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean removeFollowRequestFrom(User user) {
        if (user == null)
            return false;
        return followRequests.removeIf(followRequest -> Objects.equals(followRequest.getFromUser(), user));
    }
}