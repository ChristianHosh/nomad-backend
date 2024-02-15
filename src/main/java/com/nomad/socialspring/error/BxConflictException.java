package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

public class BxConflictException extends BxException {
    protected BxConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
