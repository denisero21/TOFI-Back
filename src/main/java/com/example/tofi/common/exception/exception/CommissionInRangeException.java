package com.example.tofi.common.exception.exception;

public class CommissionInRangeException extends RuntimeException {

    public CommissionInRangeException(String message) {
        super(message);
    }

    public CommissionInRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
