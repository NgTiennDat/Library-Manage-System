package com.datien.lms.handlerException;

import lombok.Getter;

@Getter
public enum ResponseCode {

    // System errors
    SYSTEM("ERR_501", "System message. Please, try later!"),
    INTERNAL_SERVER_ERROR("ERR_500", "Internal server error"),
    NO_CODE("ERR_000", "No code"),

    // Authentication and authorization errors
    INCORRECT_CURRENT_PASSWORD("ERR_300", "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH("ERR_301", "New password does not match"),
    PASSWORD_DOES_NOT_MATCH("ERR_309", "Password does not match"),
    ACCOUNT_LOCKED("ERR_302", "User account is locked"),
    ACCOUNT_DISABLED("ERR_303", "User account is disabled"),
    BAD_CREDENTIALS("ERR_304", "Login and/or password is incorrect"),
    TOKEN_EXPIRED("ERR_305", "JWT token has expired"),
    TOKEN_INVALID("ERR_306", "Invalid JWT token"),
    ACCESS_DENIED("ERR_307", "Access denied"),
    OTP_EXPIRED("ERR_308", "OTP expired"),
    OTP_NOTFOUND("ERR_310", "OTP not found"),
    USER_NOTFOUND("ERR_311", "User not found"),
    FEEDBACK_NOTFOUND("ERR_312", "Feedback not found"),
    SCHEDULE_NOTFOUND("ERR_313", "Schedule not found"),

    // Resource errors
    RESOURCE_NOT_FOUND("ERR_404", "Resource not found"),
    METHOD_NOT_ALLOWED("ERR_405", "Method not allowed"),
    ROLE_NOT_VALID("ERR_406", "Role not valid"),

    // Book-related errors
    BOOK_NOT_FOUND("ERR_601", "Book not found"),
    BOOK_NOT_AVAILABLE("ERR_602", "Book not available"),
    BOOK_ALREADY_BORROWED("ERR_603", "Book already borrowed"),
    OPERATION_NOT_PERMITTED("ERR_604", "Operation not permitted"),

    // Email-related errors
    EMAIL_ERROR("ERR_700", "Email error"),
    EMAIL_NOT_FOUND("ERR_701", "Email not found"),
    EMAIL_ALREADY_EXISTS("ERR_702", "Email already exists"),
    EMAIL_SEND_FAILED("ERR_703", "Failed to send email");


    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
