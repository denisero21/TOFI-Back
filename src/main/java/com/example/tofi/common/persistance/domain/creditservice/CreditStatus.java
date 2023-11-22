package com.example.tofi.common.persistance.domain.creditservice;

import lombok.Getter;
@Getter
public enum CreditStatus {
    NEW(0),
    APPROVED(1),
    PAID(2);


    private final int code;

    CreditStatus(int value) {
        this.code = value;
    }

}
