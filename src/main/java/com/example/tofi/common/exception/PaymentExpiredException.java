package com.example.tofi.common.exception;

public class PaymentExpiredException extends RuntimeException {

    public PaymentExpiredException(String message) {
        super(message);
    }

    public PaymentExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
