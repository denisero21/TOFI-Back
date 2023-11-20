package com.example.tofi.common.persistance.domain.userservice.dto.email;

public enum DefaultEmailText {
    RESET_PASSWORD_TEXT("Reset password link:"),
    ONE_TIME_PASSWORD_TEXT("One time password:");

    private final String textName;

    DefaultEmailText(String textName) {
        this.textName = textName;
    }

    public String getTextName() {
        return textName;
    }
}