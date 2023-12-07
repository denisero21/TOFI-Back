package com.example.tofi.credit.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.CreditTerm;
import com.example.tofi.common.persistance.domain.creditservice.PaymentType;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.domain.creditservice.dto.MakePaymentRequest;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.persistance.repository.CreditRepository;
import com.example.tofi.common.service.CountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CountService countService;

    @Mock
    private SchedulerJobService jobService;

    @InjectMocks
    private CreditService creditService;

    private CreateCreditDto createCreditDto;
    private Credit credit;
    private Account account;
    private MakePaymentRequest makePaymentRequest;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(2000.0);
        account.setIsBlocked(false);

        credit = new Credit();
        credit.setId(1L);
        credit.setAccountId(1L);
        credit.setNextPayDate(LocalDateTime.now().minusDays(1));
        credit.setPerMonthPaySum(100.0);
        credit.setDebt(1000.0);
        credit.setPaymentType(PaymentType.MANUAL);

        createCreditDto = new CreateCreditDto();
        createCreditDto.setPaymentType(PaymentType.AUTO);
        createCreditDto.setAmountGiven(1000.0);
        createCreditDto.setTerm(CreditTerm.MONTH_3);
        createCreditDto.setAccountId(1L);

        makePaymentRequest = new MakePaymentRequest();
        makePaymentRequest.setSumToPay(100.0);


    }

    @Test
    public void testMakePaymentForCreditWhenPaymentIsSuccessfulThenReduceBalanceAndDebt() {
        when(creditRepository.findById(anyLong())).thenReturn(Optional.of(credit));
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        creditService.makePaymentForCredit(1L, makePaymentRequest);

        assertEquals(1900.0, account.getBalance());
        assertEquals(900.0, credit.getDebt());
        verify(creditRepository, times(1)).save(any(Credit.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testMakePaymentForCreditWhenAccountHasInsufficientBalanceThenThrowException() {
        account.setBalance(50.0);
        when(creditRepository.findById(anyLong())).thenReturn(Optional.of(credit));
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Exception exception = assertThrows(RuntimeException.class, () -> creditService.makePaymentForCredit(1L, makePaymentRequest));
        assertEquals("Not enough money on bank account ( Иди работай бомжара)", exception.getMessage());
        verify(creditRepository, times(0)).save(any(Credit.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testMakePaymentForCreditWhenNextPaymentDateIsInFutureThenThrowException() {
        credit.setNextPayDate(LocalDateTime.now().plusDays(1));
        when(creditRepository.findById(anyLong())).thenReturn(Optional.of(credit));

        Exception exception = assertThrows(RuntimeException.class, () -> creditService.makePaymentForCredit(1L, makePaymentRequest));
        assertEquals("Too early to pay", exception.getMessage());
        verify(creditRepository, times(0)).save(any(Credit.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testMakePaymentForCreditWhenAccountIsBlockedThenThrowRuntimeException() {
        account.setIsBlocked(true);
        when(creditRepository.findById(anyLong())).thenReturn(Optional.of(credit));
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Exception exception = assertThrows(RuntimeException.class, () -> creditService.makePaymentForCredit(1L, makePaymentRequest));
        assertEquals("Operation error.Account is blocked.", exception.getMessage());
        verify(creditRepository, times(0)).save(any(Credit.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    public void testGetCreditPaymentInfoWhenCreditIsFoundThenReturnCreditPaymentInfoDto() {
        Credit credit = new Credit();
        credit.setId(1L);
        credit.setName("Test Credit");
        credit.setPerMonthPaySum(100.0);
        credit.setDebt(1000.0);
        credit.setNextPayDate(LocalDateTime.now().minusDays(1));

        when(creditRepository.findById(anyLong())).thenReturn(Optional.of(credit));
        when(countService.calculatePenalty(anyDouble(), anyLong())).thenReturn(10.0);

        var creditPaymentInfoDto = creditService.getCreditPaymentInfo(1L);

        assertEquals(credit.getId(), creditPaymentInfoDto.getCreditId());
        assertEquals(credit.getName(), creditPaymentInfoDto.getCreditName());
        assertEquals(credit.getPerMonthPaySum(), creditPaymentInfoDto.getSumPerMonth());
        assertEquals(10.0, creditPaymentInfoDto.getPenya());
        assertEquals(credit.getDebt() - credit.getPerMonthPaySum(), creditPaymentInfoDto.getDebtAfterPayment());
        assertEquals(credit.getPerMonthPaySum() + 10.0, creditPaymentInfoDto.getSumToPay());
    }

    @Test
    public void testGetCreditPaymentInfoWhenCreditIsNotFoundThenThrowRuntimeException() {
        when(creditRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditService.getCreditPaymentInfo(1L));
    }
}