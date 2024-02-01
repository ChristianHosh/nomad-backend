package com.nomad.socialspring.common;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@Getter
public abstract class BaseResponse {
  private final Long id;
  private final Timestamp createdOn;
  private final Timestamp updatedOn;
  
  protected BaseResponse(@NotNull BaseEntity entity) {
    this.id = entity.getId();
    this.createdOn = entity.getCreatedOn();
    this.updatedOn = entity.getUpdatedOn();
  }
}
