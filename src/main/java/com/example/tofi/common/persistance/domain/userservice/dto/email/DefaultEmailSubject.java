package com.example.tofi.common.persistance.domain.userservice.dto.email;

public enum DefaultEmailSubject {
    RESET_PASSWORD("Reset password"),
    ONE_TIME_PASSWORD("One time password");

    private String subjectName;

    DefaultEmailSubject(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }
}