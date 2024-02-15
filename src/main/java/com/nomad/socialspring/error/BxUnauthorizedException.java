package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

public class BxUnauthorizedException extends BxException {
  public BxUnauthorizedException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
