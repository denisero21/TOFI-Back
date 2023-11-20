package com.example.tofi.common.exception;

public class RoleStillAssignedToUsersException extends RuntimeException {
    public RoleStillAssignedToUsersException(String message) {
        super(message);
    }
}