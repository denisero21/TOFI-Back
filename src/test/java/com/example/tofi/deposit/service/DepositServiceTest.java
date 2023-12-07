package com.example.tofi.deposit.service;

import com.example.tofi.account.service.AccountService;
import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.depositservice.Deposit;
import com.example.tofi.common.persistance.domain.depositservice.DepositStatus;
import com.example.tofi.common.persistance.domain.depositservice.DepositTerm;
import com.example.tofi.common.persistance.domain.depositservice.DepositType;
import com.example.tofi.common.persistance.domain.depositservice.dto.CreateDepositDto;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.persistance.repository.DepositRepository;
import com.example.tofi.common.service.CountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {

    @Mock
    private DepositRepository depositRepository;

    @Mock
    private CountService countService;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DepositService depositService;

    private Account account;
    private CreateDepositDto depositDto;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);

        depositDto = new CreateDepositDto();
        depositDto.setAccountId(1L);
        depositDto.setAmount(500.0);
        depositDto.setTerm(DepositTerm.MONTH_3);
        depositDto.setType(DepositType.IRREVOCABLE);
        depositDto.setName("Test Deposit");
    }

    @Test
    public void testCreateDepositWhenNotEnoughBalanceThenRuntimeException() {
        depositDto.setAmount(2000.0);
        when(accountRepository.findById(depositDto.getAccountId())).thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class, () -> depositService.createDeposit(1L, depositDto));
    }

    @Test
    public void testCloseDepositWhenDepositNotFoundThenRuntimeException() {
        when(depositRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> depositService.closeDeposit(1L));
    }

    @Test
    public void testCloseDepositWhenDepositAlreadyClosedThenRuntimeException() {
        Deposit deposit = new Deposit();
        deposit.setStatus(DepositStatus.CLOSED);
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));

        assertThrows(RuntimeException.class, () -> depositService.closeDeposit(1L));
    }

    @Test
    public void testCloseDepositWhenIrrevocableDepositClosedTooEarlyThenRuntimeException() {
        Deposit deposit = new Deposit();
        deposit.setType(DepositType.IRREVOCABLE);
        deposit.setTerm(DepositTerm.MONTH_3);
        deposit.setDate(LocalDateTime.now().minusMonths(1));
        deposit.setStatus(DepositStatus.ONCOMPENSATION);
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));

        assertThrows(RuntimeException.class, () -> depositService.closeDeposit(1L));
    }

    @Test
    public void testCloseDepositWhenIrrevocableDepositClosedAndNotTooEarlyThenTransferAmountToAccountBalance() {
        Deposit deposit = new Deposit();
        deposit.setType(DepositType.IRREVOCABLE);
        deposit.setTerm(DepositTerm.MONTH_3);
        deposit.setDate(LocalDateTime.now().minusMonths(4));
        deposit.setStatus(DepositStatus.ONCOMPENSATION);
        deposit.setAccountId(1L);
        deposit.setAmount(500.0);
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));
        when(countService.calculateCompensationAmountForIrrevocableDeposit(
                deposit.getAmount(), deposit.getTerm().getTerm(), deposit.getTerm().getIrrevocablePercent()))
                .thenReturn(600.0);

        depositService.closeDeposit(1L);

        verify(accountService).getAmountFromDeposit(deposit.getAccountId(), deposit.getCompensationAmount());
        verify(depositRepository).save(deposit);
    }

    @Test
    public void testCloseDepositWhenRevocableDepositClosedThenTransferAmountToAccountBalance() {
        Deposit deposit = new Deposit();
        deposit.setType(DepositType.REVOCABLE);
        deposit.setTerm(DepositTerm.MONTH_3);
        deposit.setDate(LocalDateTime.now().minusMonths(1));
        deposit.setStatus(DepositStatus.ONCOMPENSATION);
        deposit.setAccountId(1L);
        deposit.setAmount(500.0);
        when(depositRepository.findById(1L)).thenReturn(Optional.of(deposit));
        when(countService.calculateCompensationAmountForRevocableDeposit(
                deposit.getAmount(), 30, deposit.getTerm().getRevocablePercent()))
                .thenReturn(600.0);

        depositService.closeDeposit(1L);

        verify(accountService).getAmountFromDeposit(deposit.getAccountId(), deposit.getCompensationAmount());
        verify(depositRepository).save(deposit);
    }
}