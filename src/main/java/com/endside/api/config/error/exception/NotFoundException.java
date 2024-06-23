package com.endside.api.config.error.exception;

import com.endside.api.config.error.ErrorCode;

public class NotFoundException extends RestException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
