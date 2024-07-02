package com.endside.api.config.error.exception;


import com.endside.api.config.error.ErrorCode;

public class LoginFailureException extends RestException {
    public LoginFailureException() {
        super();
    }

    public LoginFailureException(ErrorCode errorCode) {
        super(errorCode);
    }
}
