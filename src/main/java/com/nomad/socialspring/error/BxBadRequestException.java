package com.nomad.socialspring.error;

import org.springframework.http.HttpStatus;

public class BxBadRequestException extends BxException {
    protected BxBadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
