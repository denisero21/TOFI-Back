package com.example.tofi.common.exception;

public class AgentFieldRestrictionException extends RuntimeException {
    public AgentFieldRestrictionException(String message) {
        super(message);
    }

    public AgentFieldRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
