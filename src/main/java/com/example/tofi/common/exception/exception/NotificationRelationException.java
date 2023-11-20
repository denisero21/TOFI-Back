package com.example.tofi.common.exception.exception;

public class NotificationRelationException extends RuntimeException {

    public NotificationRelationException(String message) {
        super(message);
    }

    public NotificationRelationException(String message, Throwable cause) {
        super(message, cause);
    }
}
