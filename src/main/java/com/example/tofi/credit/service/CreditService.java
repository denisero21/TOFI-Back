package com.example.tofi.credit.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.CreditStatus;
import com.example.tofi.common.persistance.domain.creditservice.PaymentType;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.persistance.repository.CreditRepository;
import com.example.tofi.common.service.CountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;
    private final AccountRepository accountRepository;
    private final CountService countService;

    public void createCredit(Long userId, CreateCreditDto createCreditDto) {
        Credit credit = new Credit();
        BeanUtils.copyProperties(createCreditDto, credit);
        credit.setDate(LocalDateTime.now());
        credit.setDebt(credit.getAmountGiven());
        credit.setNextPayDate(LocalDateTime.now().plusMonths(1));
        credit.setUserId(userId);
        credit.setStatus(CreditStatus.NEW);
        credit.setPerMonthPaySum(countService.countPerMonthPaySum(
                createCreditDto.getAmountGiven(),
                createCreditDto.getTerm().getTerm(),
                createCreditDto.getTerm().getPercent()));
        creditRepository.save(credit);
        if (createCreditDto.getPaymentType().equals(PaymentType.AUTO)) {
            // TODO: 27.11.2023 create quartz job
        }

    }

    public void makePaymentForCredit(Long id) {
        Credit credit = creditRepository.findById(id).orElseThrow(RuntimeException::new);
        if(credit.getNextPayDate().toLocalDate().isAfter(LocalDate.now())) throw new RuntimeException("Too early to pay");
        Account account = accountRepository.findById(credit.getAccountId()).orElseThrow(RuntimeException::new);
        Double paySum = credit.getPerMonthPaySum();
        if (account.getBalance() >= paySum) {
            account.setBalance(account.getBalance() - credit.getPerMonthPaySum());
            credit.setDebt(credit.getDebt() - paySum);
        } else {
            // TODO: 27.11.2023 чет надо сделать
            throw new RuntimeException("Not enough money on bank account ( Иди работай бомжара)");
        }
        if (credit.getDebt().equals(0.0)) {
            credit.setStatus(CreditStatus.PAID);
        } else {
            credit.setNextPayDate(credit.getNextPayDate().plusMonths(1));
        }
        creditRepository.save(credit);
        accountRepository.save(account);
    }


    public List<Credit> getUsersCredits(Long userId) {
        return creditRepository.findAllByUserId(userId);
    }


}
