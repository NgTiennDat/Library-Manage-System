package com.datien.lms.handlerException;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SYSTEM("ERR_501", "System message. Please, try later!"),
    NO_CODE("ERR_000", "No code"),
    INCORRECT_CURRENT_PASSWORD("ERR_300", "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH("ERR_301", "New password does not match"),
    ACCOUNT_LOCKED("ERR_302", "User account is locked"),
    ACCOUNT_DISABLED("ERR_303", "User account is disabled"),
    BAD_CREDENTIALS("ERR_304", "Login and/or password is incorrect"),

    // New error codes
    TOKEN_EXPIRED("ERR_305", "JWT token has expired"),
    TOKEN_INVALID("ERR_306", "Invalid JWT token"),
    ACCESS_DENIED("ERR_307", "Access denied"),
    RESOURCE_NOT_FOUND("ERR_404", "Resource not found"),
    METHOD_NOT_ALLOWED("ERR_405", "Method not allowed"),
    INTERNAL_SERVER_ERROR("ERR_500", "Internal server error");


    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
