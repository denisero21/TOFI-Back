package com.example.tofi.common.exception;

public class PaylinkBlockedException extends RuntimeException {
    public PaylinkBlockedException(String message) {
        super(message);
    }

    public PaylinkBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}