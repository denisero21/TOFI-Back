package com.example.tofi.common.exception.exception;


import lombok.Getter;

@Getter
public class CommissionNotFoundException extends RuntimeException {

    public CommissionNotFoundException(String message) {
        super(message);
    }

    public CommissionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
