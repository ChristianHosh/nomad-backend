package com.nomad.socialspring.error.exceptions;

import com.nomad.socialspring.common.BaseEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BxException extends RuntimeException {
    protected BxException(String message) {
        super(message);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException notFound(@NotNull Class<? extends BaseEntity> entityClass, @NotNull Object value) {
        return new BxNotFoundException(entityClass.getName() + ": not found for '" + value + "'");
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BxException conflict(@NotNull Class<? extends BaseEntity> entityClass, @NotNull Object field, @NotNull Object value) {
        return new BxConflictException(entityClass.getName() + ": " + field + " already exists for '" + value + "'");
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BxException badRequest(@NotNull Class<? extends BaseEntity> entityClass, @NotNull Object field, @NotNull Object value) {
        return new BxBadRequestException(entityClass.getName() + ": " + field + " " + value);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException badRequest(@NotNull Class<? extends BaseEntity> entityClass, @NotNull Object value) {
        return new BxBadRequestException(entityClass.getName() + ": " + value);
    }

    @NotNull
    @Contract("_ -> new")
    public static BxException hardcoded(String message) {
        return new BxException(message);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BxException hardcoded(@NotNull Class<? extends BaseEntity> entityClass, String message) {
        return new BxException(entityClass.getName() + ": " + message);
    }
}
