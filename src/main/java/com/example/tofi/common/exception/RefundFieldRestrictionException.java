package com.example.tofi.common.exception;

public class RefundFieldRestrictionException extends RuntimeException {
    public RefundFieldRestrictionException(String message) {
        super(message);
    }

    public RefundFieldRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
