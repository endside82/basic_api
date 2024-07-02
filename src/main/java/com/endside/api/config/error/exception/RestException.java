package com.endside.api.config.error.exception;


import com.endside.api.config.error.ErrorCode;
import lombok.Getter;

@Getter
public class RestException extends RuntimeException {
    private ErrorCode errorCode;

    public RestException() {
        super();
    }

    public RestException(final String message) {
        super(message);
    }

    public RestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
