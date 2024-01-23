package com.nomad.socialspring.error.exceptions;

import com.nomad.socialspring.common.BaseEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class BxException extends RuntimeException {

    public static final String X_CURRENT_USER_NOT_IN_CHAT = "You are not in this chat";
    public static final String X_ACCOUNT_NOT_VERIFIED = "Your account is not verified";
    public static final String X_DATE_NOT_EXPIRED = "Still not expired, check spam inbox";
    public static final String X_CURRENT_USER_ALREADY_FOLLOWS = "You are already following this user";
    public static final String X_CURRENT_USER_ALREADY_UNFOLLOWS = "You are already not following this user";
    public static final String X_NOT_LOGGED_IN = "You are not logged in";
//    public static final String X_


    protected BxException(String message) {
        super(message);
    }

    protected BxException(Throwable cause) {
        super(cause);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException notFound(@NotNull Class<?> clazz, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        return new BxNotFoundException(clazz.getSimpleName() + ": not found for '" + value + "'");
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xNotFound(Class<?> clazz, Object value) {
        return () -> {
            throw BxException.notFound(clazz, value);
        };
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BxException conflict(@NotNull Class<?> clazz, @NotNull Object field, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        return new BxConflictException(clazz.getSimpleName() + ": " + field + " already exists for '" + value + "'");
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xConflict(Class<?> clazz, @NotNull Object field, @NotNull Object value) {
        return () -> {
            throw BxException.conflict(clazz, field, value);
        };
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BxException badRequest(@NotNull Class<?> clazz, @NotNull Object field, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        return new BxBadRequestException(clazz.getSimpleName() + ": " + field + " " + value);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException badRequest(@NotNull Class<?> clazz, @NotNull Object value) {
        if (value instanceof BaseEntity)
            value = value.getClass().getSimpleName() + ": " + ((BaseEntity) value).getExceptionString();
        return new BxBadRequestException(clazz.getSimpleName() + ": " + value);
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xBadRequest(Class<?> clazz, @NotNull Object field, @NotNull Object value) {
        return () -> {
            throw BxException.badRequest(clazz, field, value);
        };
    }

    @NotNull
    @Contract(pure = true)
    public static Supplier<? extends RuntimeException> xBadRequest(Class<?> clazz, @NotNull Object value) {
        return () -> {
            throw BxException.badRequest(clazz, value);
        };
    }

    @NotNull
    @Contract("null -> new")
    public static BxException unauthorized(Object value) {
        if (value instanceof BaseEntity)
            value = ((BaseEntity) value).getExceptionString();
        return new BxUnauthorizedException("you are unauthorized to do this action: " + value);
    }

    @NotNull
    @Contract("_ -> new")
    public static BxSevereException hardcoded(String message) {
        return new BxSevereException(message);
    }

    public static Supplier<? extends RuntimeException> xHardcoded(String message) {
        return () -> {
            throw BxSevereException.hardcoded(message);
        };
    }

    @NotNull
    @Contract("_ -> new")
    public static BxSevereException unexpected(Exception e) {
        return new BxSevereException("Internal Server Error: " + e.getMessage());
    }

    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
