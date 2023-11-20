package com.example.tofi.common.exception;

public class PaymentStatusException extends RuntimeException {

    public PaymentStatusException(String message) {
        super(message);
    }

    public PaymentStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}