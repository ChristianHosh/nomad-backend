package com.nomad.socialspring.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class BaseEntity implements Persistable<Long> {

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

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
    
    public String getExceptionString() {
        Long id = getId();
        if (id == null) {
            return toString();
        }
        return getId().toString();
    }

    @Override
    public String toString() {
        return "%s | ID:%d | CREATED ON:%s | UPDATED ON:%s | IS NEW:%s".formatted(
            getClass().getSimpleName(),
            getId(),
            getCreatedOn(),
            getUpdatedOn(),
            String.valueOf(isNew)
        );
    }
}
