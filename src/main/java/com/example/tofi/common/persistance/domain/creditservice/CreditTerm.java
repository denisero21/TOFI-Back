package com.example.tofi.common.persistance.domain.creditservice;

import lombok.Getter;

@Getter
public enum CreditTerm {
    MONTH_3(0,3, 0.1),
    MONTH_6(1,6,0.1),
    MONTH_12(2,12,0.1);

    private final int code;
    private final int term;
    private final double percent;

    CreditTerm(int value,int term,double percent) {
        this.code = value;
        this.term = term;
        this.percent = percent;
    }

}
