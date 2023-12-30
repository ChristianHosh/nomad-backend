package com.nomad.socialspring.error.exceptions;

import com.nomad.socialspring.common.BaseEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class BxException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(BxException.class);

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
        LOGGER.warn(exception.getMessage(), exception);
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
        LOGGER.warn(exception.getMessage(), exception);
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
        LOGGER.warn(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException badRequest(@NotNull Class<? extends BaseEntity> clazz, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        BxBadRequestException exception = new BxBadRequestException(clazz.getSimpleName() + ": " + value);
        LOGGER.warn(exception.getMessage(), exception);
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
        LOGGER.error(exception.getMessage(), exception);
        return exception;
    }

    public static BxException hardcoded(Throwable cause) {
        BxException exception = new BxException(cause);
        LOGGER.error(exception.getMessage(), exception);
        return exception;
    }

    public static BxException hardcoded(@NotNull Class<?> clazz, Throwable cause) {
        BxException exception = new BxException(clazz.getSimpleName() + ": " + cause.getMessage());
        LOGGER.error(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException hardcoded(@NotNull Class<?> clazz, String message) {
        BxException exception = new BxException(clazz.getSimpleName() + ": " + message);
        LOGGER.error(exception.getMessage(), exception);
        return exception;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException hardcoded(@NotNull BaseEntity entity, String message) {
        BxException exception = new BxException(entity.getClass().getSimpleName() + ": " + entity.getExceptionString() + ": " + message);
        LOGGER.error(exception.getMessage(), exception);
        return exception;
    }

}
