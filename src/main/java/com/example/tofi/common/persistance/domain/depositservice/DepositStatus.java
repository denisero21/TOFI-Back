package com.example.tofi.common.persistance.domain.depositservice;

import lombok.Getter;

@Getter
public enum DepositStatus {
    NEW(0),
    APPROVED(1),
    ONCOMPENSATION(2),
    CLOSED(3);


    private final int code;

    DepositStatus(int value) {
        this.code = value;
    }
}
