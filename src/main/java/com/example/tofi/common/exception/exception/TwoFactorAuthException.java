package com.example.tofi.common.exception.exception;

public class TwoFactorAuthException extends RuntimeException {
    public TwoFactorAuthException(String message) {
        super(message);
    }
}
