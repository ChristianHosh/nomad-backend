package com.nomad.socialspring.error;

import com.nomad.socialspring.error.exceptions.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConExceptionHandler {

    public ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(status, message));
    }

    public ResponseEntity<ApiError> buildErrorResponse(@NotNull BxException exception) {
        return buildErrorResponse(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(BxException.class)
    public ResponseEntity<ApiError> handleNotFound(BxException exception) {
        return buildErrorResponse(exception);
    }

}
