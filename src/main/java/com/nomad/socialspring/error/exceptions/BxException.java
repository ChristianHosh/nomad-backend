package com.nomad.socialspring.error.exceptions;

import com.nomad.socialspring.common.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Slf4j
public class BxException extends RuntimeException {

    public static final String X_CURRENT_USER_NOT_IN_CHAT = "You are not in this chat";
    public static final String X_ACCOUNT_NOT_VERIFIED = "Your account is not verified";
    public static final String X_DATE_NOT_EXPIRED = "Still not expired, check spam inbox";
//    public static final String X_


    protected BxException(String message) {
        super(message);
    }

    protected BxException(Throwable cause) {
        super(cause);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException notFound(@NotNull Class<? extends BaseEntity> clazz, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        BxNotFoundException exception = new BxNotFoundException(clazz.getSimpleName() + ": not found for '" + value + "'");
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xNotFound(Class<? extends BaseEntity> clazz, Object value) {
        return () -> {
            throw BxException.notFound(clazz, value);
        };
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BxException conflict(@NotNull Class<? extends BaseEntity> clazz, @NotNull Object field, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        BxConflictException exception = new BxConflictException(clazz.getSimpleName() + ": " + field + " already exists for '" + value + "'");
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xConflict(Class<? extends BaseEntity> clazz, @NotNull Object field, @NotNull Object value) {
        return () -> {
            throw BxException.conflict(clazz, field, value);
        };
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BxException badRequest(@NotNull Class<? extends BaseEntity> clazz, @NotNull Object field, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        BxBadRequestException exception = new BxBadRequestException(clazz.getSimpleName() + ": " + field + " " + value);
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException badRequest(@NotNull Class<? extends BaseEntity> clazz, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        BxBadRequestException exception = new BxBadRequestException(clazz.getSimpleName() + ": " + value);
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xBadRequest(Class<? extends BaseEntity> clazz, @NotNull Object field, @NotNull Object value) {
        return () -> {
            throw BxException.badRequest(clazz, field, value);
        };
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xBadRequest(Class<? extends BaseEntity> clazz, @NotNull Object value) {
        return () -> {
            throw BxException.badRequest(clazz, value);
        };
    }

    @NotNull
    @Contract("null -> new")
    public static BxException unauthorized(Object value) {
        if (value instanceof BaseEntity)
            value = ((BaseEntity) value).getExceptionString();
        return new BxUnauthorizedException("unauthorized: " + value);
    }

    @NotNull
    @Contract("_ -> new")
    public static BxException hardcoded(String message) {
        BxException exception = new BxException(message);
        log.info(exception.getMessage(), exception);
        return exception;
    }

    public static BxException hardcoded(@NotNull Class<?> clazz, Throwable cause) {
        BxException exception = new BxException(clazz.getSimpleName() + ": " + cause.getMessage());
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException hardcoded(@NotNull Class<?> clazz, String message) {
        BxException exception = new BxException(clazz.getSimpleName() + ": " + message);
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException hardcoded(@NotNull BaseEntity entity, String message) {
        BxException exception = new BxException(entity.getClass().getSimpleName() + ": " + entity.getExceptionString() + ": " + message);
        log.info(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_ -> new")
    public static BxException unexpected(Exception e) {
        log.error(e.getMessage(), e);
        return new BxException("Internal Server Error: " + e.getMessage());
    }
}
