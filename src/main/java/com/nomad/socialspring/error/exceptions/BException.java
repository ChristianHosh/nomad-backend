package com.nomad.socialspring.error.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BException extends RuntimeException {
    public BException(String message) {
        super(message);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static BException notFound(@NotNull Class<?> entityClass, @NotNull Object value) {
        return new BNotFoundException(entityClass.getName() + " not found for '" + value + "'");
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static BException conflict(@NotNull Class<?> entityClass, @NotNull Object field, @NotNull Object value) {
        return new BConflictException(entityClass.getName() + " " + field + " already exists for '" + value + "'");
    }

    public static BException hardcoded(String message) {
        return new BException(message);
    }
}
