package com.example.tofi.common.exception;

public class PasswordNotMatchedException extends RuntimeException {
    public PasswordNotMatchedException(String message) {
        super(message);
    }

    public PasswordNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }
}