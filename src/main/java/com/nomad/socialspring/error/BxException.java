package com.nomad.socialspring.error;

import com.nomad.socialspring.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.function.Supplier;

@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("unused")
public class BxException extends RuntimeException {

  public static final String X_CURRENT_USER_NOT_IN_CHAT = "You are not in this chat";
  public static final String X_ACCOUNT_NOT_VERIFIED = "Your account is not verified";
  public static final String X_DATE_NOT_EXPIRED = "Still not expired, check spam inbox";
  public static final String X_CURRENT_USER_ALREADY_FOLLOWS = "You are already following this user";
  public static final String X_CURRENT_USER_ALREADY_UNFOLLOWS = "You are already not following this user";
  public static final String X_NOT_LOGGED_IN = "You are not logged in";
  public static final String X_COULD_NOT_ADD_USER_TO_TRIP = "Could not add user to trip";
  public static final String X_COULD_NOT_ADD_FOLLOWER = "Could not add follower";
  public static final String X_COULD_NOT_REMOVE_FOLLOWER = "Could not remove follower";
  public static final String X_COULD_NOT_LIKE_POST = "Could not like post";
  public static final String X_COULD_NOT_UNLIKE_POST = "Could not unlike post";
  public static final String X_COULD_NOT_ADD_USER_TO_CHANNEL = "Could not add user to chat channel";
  public static final String X_COULD_NOT_REMOVE_USER_FROM_CHANNEL = "Could not remove user from chat channel";
  public static final String X_COULD_NOT_REMOVE_USER_FROM_TRIP = "Could not remove user from trip";
  public static final String X_COULD_NOT_LIKE_COMMENT = "Could not like comment";
  public static final String X_COULD_NOT_UNLIKE_COMMENT = "Could not unlike comment";
  public static final String X_COULD_NOT_REMOVE_FOLLOW_REQUEST = "Could not remove follow request";
  public static final String X_COULD_NOT_BLOCK_USER = "Could not block user";
  public static final String X_COULD_NOT_UNBLOCK_USER = "Could not unblock user";
  public static final String X_INVALID_TOKEN = "Invalid Token";
  public static final String X_BAD_CREDENTIALS = "Username or password incorrect";
  public static final String X_COULD_NOT_FAVORITE_POST = "Could not favorite post";
  public static final String X_COULD_NOT_UNFAVORITE_POST = "Could not unfavorite post";
  public static final String X_USER_NOT_IN_TRIP = "User is not in trip";
  public static final String X_CANT_UPDATE_TRIP_USER_STATUS = "Can't Update TripUser Status";
  public static final Object X_USER_ALREADY_REVIEWED = "Already reviewed user";
  public static final Object X_PASSWORDS_DO_NOT_MATCH = "Passwords do not match";

  private Exception exception;

  protected BxException(String message) {
    super(message);
    this.exception = null;
  }

  protected BxException(Throwable cause) {
    super(cause);
    constructException(cause);
  }

  protected BxException(String message, Throwable cause) {
    super(message, cause);
    constructException(cause);
  }

  private static String getBaseEntityValue(BaseEntity value) {
    return value.getClass().getSimpleName() + ": " + value.getExceptionString();
  }

  @NotNull
  @Contract("_, _ -> new")
  public static BxException notFound(@NotNull Class<?> clazz, @NotNull Object value) {
    if (value instanceof BaseEntity entity)
      value = getBaseEntityValue(entity);
    return new BxNotFoundException(clazz.getSimpleName() + ": not found for [" + value + "]");
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
    if (value instanceof BaseEntity e)
      value = getBaseEntityValue(e);
    return new BxConflictException(clazz.getSimpleName() + ": " + field + " already exists for [" + value + "]");
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
    if (value instanceof BaseEntity e)
      value = getBaseEntityValue(e);
    return new BxBadRequestException(clazz.getSimpleName() + ": " + field + " [" + value + "]");
  }

  @NotNull
  @Contract("_, _ -> new")
  public static BxException badRequest(@NotNull Class<?> clazz, @NotNull Object value) {
    if (value instanceof BaseEntity e)
      value = getBaseEntityValue(e);
    return new BxBadRequestException(clazz.getSimpleName() + ": [" + value + "]");
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
    if (value instanceof BaseEntity e)
      value = getBaseEntityValue(e);
    return new BxUnauthorizedException("you are unauthorized to do this action: [" + value + "]");
  }

  @NotNull
  @Contract("_ -> new")
  public static BxSevereException hardcoded(String message) {
    return new BxSevereException(message);
  }

  @NotNull
  @Contract("_, _ -> new")
  public static BxSevereException hardcoded(String message, Object value) {
    if (value instanceof BaseEntity e)
      value = getBaseEntityValue(e);
    return new BxSevereException(message + ": [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<? extends RuntimeException> xHardcoded(String message) {
    return () -> {
      throw BxException.hardcoded(message);
    };
  }

  public static BxForbiddenException forbidden(Object value) {
    if (value instanceof BaseEntity e)
      value = getBaseEntityValue(e);
    return new BxForbiddenException("you are forbidden to do this action [" + value + "]");
  }

  @NotNull
  @Contract("_ -> new")
  public static BxSevereException unexpected(@NotNull Exception e) {
    return new BxSevereException(e.getMessage(), e);
  }

  private void constructException(Throwable cause) {
    if (cause instanceof Exception e)
      this.exception = e;
    else
      this.exception = null;
  }

  public Exception getException() {
    return Objects.requireNonNullElse(exception, this);
  }

  public HttpStatus getStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
