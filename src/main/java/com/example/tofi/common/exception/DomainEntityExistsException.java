package com.example.tofi.common.exception;


import com.example.tofi.common.persistance.domain.userservice.dto.ErrorCode;

public class DomainEntityExistsException extends RuntimeException {
    private final ErrorCode errorCode;

    public DomainEntityExistsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainEntityExistsException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

