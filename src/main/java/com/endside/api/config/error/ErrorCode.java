package com.endside.api.config.error;

import lombok.Getter;

// 12XXX 13XXX
@Getter
public enum ErrorCode {
    // API SAMPLE (1100XX)
    SAMPLE_ERROR_ONE(401, 110001, "SAMPLE_ERROR_ONE"),
    SAMPLE_ERROR_TWO(401, 110002, "SAMPLE_ERROR_TWO"),
    // Stuff (1101XX)
    STUFF_NOT_EXIST(404, 110101, "STUFF_NOT_EXIST");

    private final int code;
    private final int status;
    private final String message;

    ErrorCode(final int status, final int code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
