package com.example.tofi.common.exception.exception;

public class NotificationPriorityException extends RuntimeException {

    public NotificationPriorityException(String message) {
        super(message);
    }

    public NotificationPriorityException(String message, Throwable cause) {
        super(message, cause);
    }
}
