package com.datien.lms.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException() {}
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
