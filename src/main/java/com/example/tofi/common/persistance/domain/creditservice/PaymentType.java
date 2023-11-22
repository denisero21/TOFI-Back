package com.example.tofi.common.persistance.domain.creditservice;

import lombok.Getter;
@Getter
public enum PaymentType {
    AUTO(0),
    MANUAL(1);

    private final int code;

    PaymentType(int value) {
        this.code = value;
    }

}
