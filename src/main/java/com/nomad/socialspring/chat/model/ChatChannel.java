package com.nomad.socialspring.chat.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.trip.model.Trip;
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

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @OneToMany(mappedBy = "chatChannel", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @Builder.Default
    private Set<ChatChannelUser> chatChannelUsers = new LinkedHashSet<>();

    @Override
    public String getExceptionString() {
        return getId().toString();
    }
}