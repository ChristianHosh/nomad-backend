package com.nomad.socialspring.error;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.StringJoiner;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    public ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String message, Boolean isSevere) {
        return ResponseEntity.status(status).body(new ApiError(status, message, isSevere));
    }

    public ResponseEntity<ApiError> buildErrorResponse(@NotNull BxException exception) {
        return buildErrorResponse(exception, exception.getStatus());
    }

    public ResponseEntity<ApiError> buildErrorResponse(@NotNull BxException exception, HttpStatus status) {
        return buildErrorResponse(exception, status, exception instanceof BxSevereException);
    }

    public ResponseEntity<ApiError> buildErrorResponse(@NotNull BxException exception, HttpStatus status, Boolean isSevere) {
        if (isSevere)
            log.error(exception.getMessage(), exception.getException());
        else
            log.warn(exception.getMessage(), exception.getException());

        return buildErrorResponse(status, exception.getMessage(), isSevere);
    }

    @ExceptionHandler(BxException.class)
    public ResponseEntity<ApiError> handleBxExceptions(BxException exception) {
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return buildErrorResponse(BxException.hardcoded(getFieldErrors(exception)), HttpStatus.BAD_REQUEST, false);
    }

    private String getFieldErrors(@NotNull MethodArgumentNotValidException exception) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        exception.getFieldErrors().forEach(fieldError -> stringJoiner.add(fieldError.getDefaultMessage()));
        return stringJoiner.toString();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadSizeExceeded(@NotNull MaxUploadSizeExceededException exception) {
        return buildErrorResponse(BxException.hardcoded(exception.getMessage()), HttpStatus.BAD_REQUEST, false);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedExceptions(Exception exception) {
        return buildErrorResponse(BxException.unexpected(exception));
    }

}
