package com.example.tofi.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountService {
    public Double countPerMonthPaySum(Double sum,Integer months, Double percent ){
        // Пример параметров кредита
        double loanAmount = sum; // Сумма кредита
        double annualInterestRate = percent / 100; // Годовая процентная ставка (5%)
        int loanTermInMonths = months; // Срок кредита в месяцах

        // Расчет ежемесячного платежа
        double monthlyInterestRate = annualInterestRate / 12;

        return loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermInMonths))
                / (Math.pow(1 + monthlyInterestRate, loanTermInMonths) - 1);
    }


}
