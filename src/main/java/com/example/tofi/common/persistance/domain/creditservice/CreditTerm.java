package com.example.tofi.common.persistance.domain.creditservice;

import lombok.Getter;

@Getter
public enum CreditTerm {
    MONTH_3(0,3, 10),
    MONTH_6(1,6,10),
    MONTH_12(2,12,10);

    private final int code;
    private final int term;
    private final int percent;

    CreditTerm(int value,int term,int percent) {
        this.code = value;
        this.term = term;
        this.percent = percent;
    }

}
