package com.example.tofi.common.exception.exception;

public class SavedCardNotFoundException extends RuntimeException {

    public SavedCardNotFoundException(String message) {
        super(message);
    }

    public SavedCardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}