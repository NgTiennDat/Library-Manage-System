package com.datien.lms.handlerException;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SYSTEM("ERR_5001", "System message. Please, try later!"),
    NO_CODE("ERR_000", "No code"),
    INCORRECT_CURRENT_PASSWORD("ERR_300", "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH("ERR_301", "New password does not match"),
    ACCOUNT_LOCKED("ERR_302", "User account is locked"),
    ACCOUNT_DISABLED("ERR_303", "User account is disabled"),
    BAD_CREDENTIALS("ERR_304", "Login and/or password is incorrect");


    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
