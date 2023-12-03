package com.example.tofi.common.persistance.domain.depositservice;

import lombok.Getter;

@Getter
public enum DepositTerm {
    MONTH_3(0,3, 6.72, 10.21),
    MONTH_6(1,6,6.72, 10.21),
    MONTH_12(2,12,6.72, 10.21),
    PERPETUAL(3, 999, 6.72, 10.21);

    private final int code;
    private final int term;
    private final double revocablePercent;
    private final double irrevocablePercent;

    DepositTerm(int value,int term,double revocablePercent, double irrevocablePercent) {
        this.code = value;
        this.term = term;
        this.revocablePercent = revocablePercent;
        this.irrevocablePercent = irrevocablePercent;
    }
}
