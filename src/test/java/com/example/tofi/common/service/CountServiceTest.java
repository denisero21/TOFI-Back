package com.example.tofi.common.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CountServiceTest {

    @InjectMocks
    private CountService countService;

    @Test
    public void testGetIdWhenCalledThenReturnStringOfLength20() {
        // Arrange (not needed in this case)
        // Act
        String id = countService.getId();
        // Assert
        assertThat(id).hasSize(20);
    }

    @Test
    public void testCountPerMonthPaySumWhenCalledThenReturnCorrectMonthlyPaymentAmount() {
        // Arrange
        Double sum = 10000.0;
        Integer months = 12;
        Double percent = 5.0;

        // Act
        Double monthlyPayment = countService.countPerMonthPaySum(sum, months, percent);

        // Assert
        Double expectedMonthlyPayment = sum * ((percent / 12) * Math.pow(1 + (percent / 12), months))
                / (Math.pow(1 + (percent / 12), months) - 1);
        assertThat(monthlyPayment).isEqualTo(expectedMonthlyPayment);
    }

    @Test
    public void testCalculatePenaltyWhenCalledThenReturnCorrectPenaltyAmount() {
        // Arrange
        Double amount = 5000.0;
        Long daysOverdue = 30L;

        // Act
        Double penalty = countService.calculatePenalty(amount, daysOverdue);

        // Assert
        Double expectedPenalty = amount * (0.1 / 100) * (daysOverdue / 365);
        assertThat(penalty).isEqualTo(expectedPenalty);
    }

    @Test
    public void testCalculateCompensationAmountForIrrevocableDepositWhenCalledThenReturnCorrectCompensationAmount() {
        // Arrange
        Double sum = 10000.0;
        Integer months = 6;
        Double percent = 4.0;

        // Act
        Double compensationAmount = countService.calculateCompensationAmountForIrrevocableDeposit(sum, months, percent);

        // Assert
        Double expectedCompensationAmount = sum + (sum * (months) * (percent / 100));
        assertThat(compensationAmount).isEqualTo(expectedCompensationAmount);
    }

    @Test
    public void testCalculateCompensationAmountForRevocableDepositWhenCalledThenReturnCorrectCompensationAmount() {
        // Arrange
        Double sum = 10000.0;
        Integer days = 90;
        Double percent = 3.0;

        // Act
        Double compensationAmount = countService.calculateCompensationAmountForRevocableDeposit(sum, days, percent);

        // Assert
        Double expectedCompensationAmount = sum + (sum * (days / 30) * (percent / 100));
        assertThat(compensationAmount).isEqualTo(expectedCompensationAmount);
    }
}
