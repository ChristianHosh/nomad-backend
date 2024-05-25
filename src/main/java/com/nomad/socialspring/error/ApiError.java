package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

public record ApiError(
    Timestamp timestamp,
    String error,
    String message,
    Integer status,
    Boolean isSevere

) {

  public ApiError(HttpStatus status, String message) {
    this(status, message, false);
  }

  public ApiError(HttpStatus status, String message, Boolean isSevere) {
    this(Timestamp.from(Instant.now()), status.getReasonPhrase(), message, status.value(), isSevere);
  }

  public static ApiError of(BxException e) {
    return new ApiError(e.getStatus(), e.getMessage(), e instanceof BxSevereException);
  }
}