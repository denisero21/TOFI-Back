package com.example.tofi.common.exception;

public class BankRequestException extends RuntimeException {

    public BankRequestException(String message) {
        super(message);
    }

    public BankRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
