package com.example.tofi.common.exception.exception;

public class SendingEmailFailedException extends RuntimeException {
    public SendingEmailFailedException(String message) {
        super(message);
    }
}