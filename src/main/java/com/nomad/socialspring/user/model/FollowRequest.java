package com.nomad.socialspring.user.model;

import com.nomad.socialspring.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_FOLLOW_REQUEST")
public class FollowRequest extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "FROM_USER_ID", nullable = false)
    private User fromUser;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "TO_USER_ID", nullable = false)
    private User toUser;

    public boolean isForUser(User user) {
        return toUser.equals(user);
    }

}