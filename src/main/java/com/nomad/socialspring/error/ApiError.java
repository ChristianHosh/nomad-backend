package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

public record ApiError (
        Timestamp timestamp,
        String error,
        String message,
        Integer status

) {

    public ApiError(HttpStatus status, String message){
        this(Timestamp.from(Instant.now()), status.getReasonPhrase(), message, status.value());
    }
}