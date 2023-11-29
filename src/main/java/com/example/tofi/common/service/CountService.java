package com.example.tofi.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CountService {

    public String getId() {
        Random random = new Random();
        int[] digits = new int[20];
        for (int i = 0; i < 20; i++) {
            digits[i] = random.nextInt(10);
        }
        StringBuilder cardNumber = new StringBuilder(String.join("", Integer.toString(digits[0])));
        for (int i = 1; i < 20; i++) {
            cardNumber.append(digits[i]);
        }
       return cardNumber.toString();
    }

    public Double countPerMonthPaySum(Double sum,Integer months, Double percent ){
        // Пример параметров кредита
        double loanAmount = sum; // Сумма кредита
        double annualInterestRate = percent ; // Годовая процентная ставка (5%)
        int loanTermInMonths = months; // Срок кредита в месяцах

        // Расчет ежемесячного платежа
        double monthlyInterestRate = annualInterestRate / 12;

        return loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermInMonths))
                / (Math.pow(1 + monthlyInterestRate, loanTermInMonths) - 1);
    }

    public Double calculatePenalty(Double amount, Integer daysOverdue) {
        double interestRate = 0.1;
        // Рассчитываем штраф по формуле: сумма * (процентная ставка за просрочку / 100) * (количество дней просрочки / 365)
        return amount * (interestRate / 100) * (daysOverdue / 365);
    }


}
