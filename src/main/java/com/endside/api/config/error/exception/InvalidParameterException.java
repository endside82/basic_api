package com.endside.api.config.error.exception;


import com.endside.api.config.error.ErrorCode;

public class InvalidParameterException extends RestException {
    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(ErrorCode errorCode) {
        super(errorCode);
    }
}