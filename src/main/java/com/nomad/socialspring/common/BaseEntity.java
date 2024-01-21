package com.nomad.socialspring.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "CREATED_ON", updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "UPDATED_ON")
    private Timestamp updatedOn;


    public String getExceptionString() {
        return getId().toString();
    }
}
