package com.example.tofi.common.persistance.domain.accountservice.dto;

public class Test {

    public static void main(String[] args) {
        // Пример параметров кредита
        double loanAmount = 10000; // Сумма кредита
        double annualInterestRate = 0.05; // Годовая процентная ставка (5%)
        int loanTermInMonths = 12; // Срок кредита в месяцах

        // Расчет ежемесячного платежа
        double monthlyInterestRate = annualInterestRate / 12;
        double monthlyPayment = loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermInMonths))
                / (Math.pow(1 + monthlyInterestRate, loanTermInMonths) - 1);

        // Вывод ежемесячного платежа
        System.out.printf("Ежемесячный платеж: %.2f\n", monthlyPayment);

        // Имитация погашения кредита
        double remainingLoanAmount = loanAmount;
        for (int month = 1; month <= loanTermInMonths; month++) {
            double interestPayment = remainingLoanAmount * monthlyInterestRate;
            double principalPayment = monthlyPayment - interestPayment;
            remainingLoanAmount -= principalPayment;

            // Вывод информации о платеже
            System.out.printf("Месяц %d: Платеж %.2f, Проценты %.2f, Остаток долга %.2f\n",
                    month, monthlyPayment, interestPayment, remainingLoanAmount);
        }
    }
}
