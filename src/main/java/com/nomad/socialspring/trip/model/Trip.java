package com.nomad.socialspring.trip.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.country.model.Country;
import com.nomad.socialspring.post.model.Post;
import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_TRIP")
public class Trip extends BaseEntity {

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "COUNTRY_ID", nullable = false)
    private Country country;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_TRIP_USERS",
            joinColumns = @JoinColumn(name = "TRIP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @Builder.Default
    private Set<User> participants = new LinkedHashSet<>();

    @OneToOne(mappedBy = "trip", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false, orphanRemoval = true)
    private Post post;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean addParticipant(User user) {
        if (user == null)
            return false;
        return participants.add(user);
    }

    public boolean removeParticipant(User user) {
        if (user == null)
            return false;
        return participants.remove(user);
    }

    public int getNumberOfParticipants() {
        return participants.size();
    }
}