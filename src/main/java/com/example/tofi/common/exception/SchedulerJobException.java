package com.example.tofi.common.exception;

public class SchedulerJobException extends RuntimeException {
    public SchedulerJobException(String message) {
        super(message);
    }

    public SchedulerJobException(String message, Throwable cause) {
        super(message, cause);
    }
}