package com.endside.api.config.error;

import lombok.Getter;

// 12XXX 13XXX
@Getter
public enum ErrorCode {

    // Common JWT api call (1000XX)
    JWT_TOKEN_AUTH_ERROR(401,100001,"JWT_TOKEN_AUTH_ERROR"),
    JWT_TOKEN_EXPIRATION(401,100002,"JWT_TOKEN_EXPIRATION"),
    INVALID_AUTH_TOKEN(401,100003,"INVALID_AUTH_TOKEN"),
    // API SAMPLE (1100XX)
    SAMPLE_ERROR_ONE(401, 110001, "SAMPLE_ERROR_ONE"),
    SAMPLE_ERROR_TWO(401, 110002, "SAMPLE_ERROR_TWO"),
    // USER LOGIN (102XX)
    LOGIN_FAILURE_USER_STATUS_EXIT(403,110205,"LOGIN_FAILURE_USER_STATUS_EXIT"),         // 탈퇴
    LOGIN_FAILURE_USER_STATUS_TRYEXIT(403,110206,"LOGIN_FAILURE_USER_STATUS_TRYEXIT"),   // 탈퇴진행
    LOGIN_FAILURE_USER_STATUS_LOGOUT(403,110207,"LOGIN_FAILURE_USER_STATUS_LOGOUT"),     // 로그아웃됨
    LOGIN_FAILURE_USER_STATUS_STOP(403,110208,"LOGIN_FAILURE_USER_STATUS_STOP"),         // 이용정지됨
    LOGIN_FAILURE_USER_STATUS_BAN(403,110209,"LOGIN_FAILURE_USER_STATUS_BAN"),           // 강제탈퇴
    LOGIN_FAILURE_BAD_CREDENTIAL(403,110210,"LOGIN_FAILURE_BAD_CREDENTIAL"),
    // Stuff (1101XX)
    STUFF_NOT_EXIST(404, 110101, "STUFF_NOT_EXIST"),
    
    // FRIEND(1103XX)
    NOT_FOUND_FRIEND(404,110301,"NOT_FOUND_FRIEND"),
    NOT_FOUND_FRIEND_REQUEST(400, 110302,"NOT_FOUND_FRIEND_REQUEST"),

    NOT_FOUND_FRIEND_BLOCK(400, 110303,"NOT_FOUND_FRIEND_BLOCK"),
    FAIL_ACCEPT_FRIEND_REQUEST(400, 110304,"FAIL_ACCEPT_FRIEND_REQUEST"),
    FAIL_ADD_FRIEND_REQUEST_ALREADY_FRIEND(400, 110305,"FAIL_ADD_FRIEND_REQUEST_ALREADY_FRIEND"),
    FAIL_ADD_FRIEND_REQUEST_BLOCKED_FRIEND_BY_USER(400, 110306,"FAIL_ADD_FRIEND_REQUEST_BLOCKED_FRIEND_BY_USER"),
    FAIL_ADD_FRIEND_REQUEST_BLOCKED_USER_BY_FRIEND(400, 110307,"FAIL_ADD_FRIEND_REQUEST_BLOCKED_USER_BY_FRIEND"),
    FAIL_ADD_FRIEND_REQUEST_ALREADY_SENT(400, 110308,"FAIL_ADD_FRIEND_REQUEST_ALREADY_SENT"),
    FAIL_ADD_FRIEND_BLOCK_NOT_EXIST(400, 110309,"FAIL_ADD_FRIEND_BLOCK_NOT_EXIST"),
    FAIL_ADD_FRIEND_BLOCK_ALREADY_BLOCK(400, 110310,"FAIL_ADD_FRIEND_BLOCK_ALREADY_BLOCK"),
    FAIL_ADD_FRIEND_REQUEST_NOT_EXIST(400, 110311,"FAIL_ADD_FRIEND_REQUEST_NOT_EXIST"),
    FAIL_ADD_FRIEND_REQUEST_NOT_ALLOWED_SELF(400, 110312,"FAIL_ADD_FRIEND_REQUEST_NOT_ALLOWED_SELF"),
    FAIL_REQUEST_TARGET_NOT_EXIST(404, 110313,"FAIL_REQUEST_TARGET_NOT_EXIST"),
    NOT_FOUND_USER(404,110314, "NOT_FOUND_USER"),
    FOUND_USER_IS_ME(401,110315, "FOUND_USER_IS_ME"),
    NO_DELETE_FRIEND(404,110316,"NO_DELETE_FRIEND"),
    FAIL_DELETE_FRIEND(400,110317,"FAIL_DELETE_FRIEND"),
    // EVENT
    FAIL_GET_USER_EVENTS(401, 110401, "FAIL_GET_USER_EVENTS"),
    ;

    private final int code;
    private final int status;
    private final String message;

    ErrorCode(final int status, final int code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
