package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

public class BxNotFoundException extends BxException {
    protected BxNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
