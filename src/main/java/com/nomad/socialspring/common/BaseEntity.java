package com.nomad.socialspring.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

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

  public String getExceptionString() {
    return toString();
  }


  @Override
  public String toString() {
    return "%s | ID:%d ".formatted(getClass().getSimpleName(), getId());
  }

  public boolean isNew() {
    return isNew;
  }

  @PrePersist
  @PreUpdate
  protected void preSave() {
    this.markNotNew();
  }

  @PostLoad
  private void markNotNew() {
    this.isNew = false;
  }

  @Override
  public final boolean equals(Object object) {
    if (this == object) return true;
    if (object == null) return false;
    Class<?> oEffectiveClass = object instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    BaseEntity that = (BaseEntity) object;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
