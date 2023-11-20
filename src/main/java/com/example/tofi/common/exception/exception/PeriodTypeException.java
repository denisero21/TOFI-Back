package com.example.tofi.common.exception.exception;

public class PeriodTypeException extends RuntimeException {
    public PeriodTypeException(String message) {
        super(message);
    }

    public PeriodTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}