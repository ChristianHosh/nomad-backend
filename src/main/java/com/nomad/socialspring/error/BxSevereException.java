package com.nomad.socialspring.error;

public class BxSevereException extends BxException {

  protected BxSevereException(String message) {
    super(message);
  }

  protected BxSevereException(Exception cause) {
    super(cause.getMessage(), cause);
  }

}
