package com.example.tofi.deposit.service;

import com.example.tofi.account.service.AccountService;
import com.example.tofi.common.persistance.domain.depositservice.Deposit;
import com.example.tofi.common.persistance.domain.depositservice.DepositStatus;
import com.example.tofi.common.persistance.domain.depositservice.DepositTerm;
import com.example.tofi.common.persistance.domain.depositservice.DepositType;
import com.example.tofi.common.persistance.domain.depositservice.dto.CreateDepositDto;
import com.example.tofi.common.persistance.repository.DepositRepository;
import com.example.tofi.common.service.CountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;
    private final CountService countService;
    private final AccountService accountService;

    public void createDeposit(Long userId, CreateDepositDto depositDto) {
        Deposit deposit = new Deposit();
        BeanUtils.copyProperties(depositDto, deposit);
        deposit.setUserId(userId);
        deposit.setDate(LocalDate.now());
        deposit.setStatus(DepositStatus.ONCOMPENSATION);
        depositRepository.save(deposit);
    }

    public void closeDeposit(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));
        deposit.setCompensationAmount(getCompensationAmountBeforeClosingDeposit(deposit));
        accountService.getAmountFromDeposit(deposit.getAccountId(), deposit.getCompensationAmount());
        deposit.setStatus(DepositStatus.CLOSED);
        depositRepository.save(deposit);
    }

    public List<Deposit> getUsersDeposits(Long userId){return depositRepository.findAllByUserId(userId);}

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
        Double amount = 0D;
        int days = 0;
        if (deposit.getTerm().equals(DepositTerm.PERPETUAL)) {
            days = Math.abs(Math.toIntExact(
                    ChronoUnit.DAYS.between(
                            deposit.getDate(),
                            LocalDate.now()
                    )));
        } else {
            days = Math.abs(Math.toIntExact(
                    ChronoUnit.DAYS.between(
                            LocalDate.now(),
                            (deposit.getDate().plusMonths(deposit.getTerm().getTerm()))
                    )));
        }
        amount = countService.calculateCompensationAmountForRevocableDeposit(
                deposit.getAmount(),
                days,
                deposit.getTerm().getRevocablePercent()
        );

        return amount;
    }
}
