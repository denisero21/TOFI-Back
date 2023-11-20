package com.example.tofi.common.persistance.domain.authservice;

import lombok.Getter;

@Getter
public enum VerificationTokenType {

    REGISTRATION("registration"),
    RESET_PASSWORD("reset_password");

    private final String value;

    VerificationTokenType(String value) {
        this.value = value;
    }
}
