package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

public class BxForbiddenException extends BxException {

  protected BxForbiddenException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.FORBIDDEN;
  }
}
