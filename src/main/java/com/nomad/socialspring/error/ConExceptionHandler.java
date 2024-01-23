package com.nomad.socialspring.error;

import com.nomad.socialspring.error.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ConExceptionHandler {

    public ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(status, message));
    }

    public ResponseEntity<ApiError> buildErrorResponse(@NotNull BxException exception) {
        if (exception instanceof BxSevereException) {
            log.error(exception.getMessage(), exception);
        } else {
            log.info(exception.getMessage(), exception);
        }
        return buildErrorResponse(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(BxException.class)
    public ResponseEntity<ApiError> handleNotFound(BxException exception) {
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleErrors(Exception exception) {
        return buildErrorResponse(BxException.unexpected(exception));
    }

}
