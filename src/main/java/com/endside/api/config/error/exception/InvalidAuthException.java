package com.endside.api.config.error.exception;


import com.endside.api.config.error.ErrorCode;

public class InvalidAuthException extends RestException {
    public InvalidAuthException() {
        super();
    }

    public InvalidAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
