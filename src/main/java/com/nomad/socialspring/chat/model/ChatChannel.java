package com.nomad.socialspring.chat.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.trip.model.Trip;
import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_CHAT_CHANNEL")
public class ChatChannel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    private UUID id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "T_CHAT_CHANNEL_USERS",
            joinColumns = @JoinColumn(name = "CHAT_CHANNEL_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<User> users = new LinkedHashSet<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @Override
    public String getExceptionString() {
        return getId().toString();
    }
}