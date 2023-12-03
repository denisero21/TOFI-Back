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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;
    private final CountService countService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public void createDeposit(Long userId, CreateDepositDto depositDto) {
        Deposit deposit = new Deposit();
        Account account = accountRepository
                .findById(depositDto.getAccountId())
                .orElseThrow(()-> new RuntimeException("Account not found"));
        if(account.getBalance() < depositDto.getAmount()){
            throw new RuntimeException("Not enough money on balance");
        }
        else{
            account.setBalance(account.getBalance() - depositDto.getAmount());
            accountRepository.save(account);
        }
        BeanUtils.copyProperties(depositDto, deposit);
        deposit.setUserId(userId);
        deposit.setDate(LocalDate.now());
        deposit.setStatus(DepositStatus.ONCOMPENSATION);
        deposit.setTerm(depositDto.getTerm());
        depositRepository.save(deposit);
    }

    public void closeDeposit(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));
        if (!deposit.getStatus().equals(DepositStatus.CLOSED)){
            if(deposit.getType().equals(DepositType.IRREVOCABLE)){
                long monthsBetween = ChronoUnit.MONTHS.between(
                        YearMonth.from(deposit.getDate()),
                        YearMonth.from(LocalDate.now())
                );
                if (monthsBetween < deposit.getTerm().getTerm()) {
                    throw new RuntimeException("Too early to close deposit");
                }else{
                    transferDepositAmountToAccountBalance(deposit);
                }
            }else{
                transferDepositAmountToAccountBalance(deposit);
            }
        }else {
            throw new RuntimeException("Deposit was closed earlier");
        }

    }

    public List<Deposit> getUsersDeposits(Long userId){return depositRepository.findAllByUserId(userId);}

    private void transferDepositAmountToAccountBalance(Deposit deposit){
        deposit.setCompensationAmount(getCompensationAmountBeforeClosingDeposit(deposit));
        accountService.getAmountFromDeposit(deposit.getAccountId(), deposit.getCompensationAmount());
        deposit.setStatus(DepositStatus.CLOSED);
        depositRepository.save(deposit);
    }

    private Double getCompensationAmountBeforeClosingDeposit(Deposit deposit) {
        return deposit.getType().equals(DepositType.IRREVOCABLE)
                ? getCompensationAmountForIrrevocableDeposit(deposit)
                : getCompensationAmountForRevocableDeposit(deposit);
    }

    private Double getCompensationAmountForIrrevocableDeposit(Deposit deposit) {
        Double amount = 0D;
        if (deposit.getTerm().equals(DepositTerm.PERPETUAL)) {
            int months = Math.abs(Math.toIntExact(
                    ChronoUnit.DAYS.between(
                            deposit.getDate(),
                            LocalDate.now()
                    ))) / 30;
            amount = countService.calculateCompensationAmountForIrrevocableDeposit(
                    deposit.getAmount(),
                    months,
                    deposit.getTerm().getIrrevocablePercent()
            );
        } else {
            amount = countService.calculateCompensationAmountForIrrevocableDeposit(
                    deposit.getAmount(),
                    deposit.getTerm().getTerm(),
                    deposit.getTerm().getIrrevocablePercent()
            );
        }

        return amount;
    }

    private Double getCompensationAmountForRevocableDeposit(Deposit deposit){
        int days = Math.abs(Math.toIntExact(
                ChronoUnit.DAYS.between(
                        deposit.getDate(),
                        LocalDate.now()

                )));

        return countService.calculateCompensationAmountForRevocableDeposit(
                deposit.getAmount(),
                days,
                deposit.getTerm().getRevocablePercent()
        );
    }
}
