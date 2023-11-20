package com.example.tofi.common.exception.exception;

public class UserFieldRestrictionException extends RuntimeException {
    public UserFieldRestrictionException(String message) {
        super(message);
    }

    public UserFieldRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
