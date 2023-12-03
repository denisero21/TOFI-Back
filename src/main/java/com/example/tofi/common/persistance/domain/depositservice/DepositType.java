package com.example.tofi.common.persistance.domain.depositservice;

import lombok.Getter;

@Getter
public enum DepositType {
    REVOCABLE(0),
    IRREVOCABLE(1);

    private final int code;

    DepositType(int value) {
        this.code = value;
    }
}
