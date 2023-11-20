package com.example.tofi.common.exception;

public class RequiredUrlParameterException extends RuntimeException {
    public RequiredUrlParameterException(String message) {
        super(message);
    }
}
