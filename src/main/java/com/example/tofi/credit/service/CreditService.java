package com.example.tofi.credit.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.CreditStatus;
import com.example.tofi.common.persistance.domain.creditservice.PaymentType;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreditPaymentInfoDto;
import com.example.tofi.common.persistance.domain.creditservice.dto.MakePaymentRequest;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.persistance.repository.CreditRepository;
import com.example.tofi.common.service.CountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        credit.setNextPayDate(LocalDateTime.now().plusMonths(1));
        credit.setUserId(userId);
        credit.setStatus(CreditStatus.APPROVED);
        credit.setPenya(0D);
        credit.setPerMonthPaySum(countService.countPerMonthPaySum(
                createCreditDto.getAmountGiven(),
                createCreditDto.getTerm().getTerm(),
                createCreditDto.getTerm().getPercent()));
        credit.setDebt(credit.getPerMonthPaySum()*createCreditDto.getTerm().getTerm());
        creditRepository.save(credit);
        if (createCreditDto.getPaymentType().equals(PaymentType.AUTO)) {
            // TODO: 27.11.2023 create quartz job
        }

    }

    public CreditPaymentInfoDto getCreditPaymentInfo(Long id){
        Credit credit = creditRepository.findById(id).orElseThrow(RuntimeException::new);
        Long days = ChronoUnit.DAYS.between(credit.getNextPayDate(), LocalDateTime.now());
        Double penya = countService.calculatePenalty(credit.getPerMonthPaySum(),days);
        CreditPaymentInfoDto creditPaymentInfoDto = new CreditPaymentInfoDto();
        creditPaymentInfoDto.setCreditId(credit.getId());
        creditPaymentInfoDto.setSum(creditPaymentInfoDto.getSum());
        creditPaymentInfoDto.setPenya(penya);
        creditPaymentInfoDto.setCreditName(credit.getName());
        creditPaymentInfoDto.setDebtAfterPayment(credit.getDebt()-credit.getPerMonthPaySum());
        return creditPaymentInfoDto;
    }

    public void makePaymentForCredit(Long id, MakePaymentRequest makePaymentRequest) {
        Credit credit = creditRepository.findById(id).orElseThrow(RuntimeException::new);
        if(credit.getNextPayDate().toLocalDate().isAfter(LocalDate.now())) throw new RuntimeException("Too early to pay");
        Account account = accountRepository.findById(credit.getAccountId()).orElseThrow(RuntimeException::new);
        Double creditPaySum = credit.getPerMonthPaySum();
        if (account.getBalance() >= makePaymentRequest.getSumToPay()) {
            account.setBalance(account.getBalance() - makePaymentRequest.getSumToPay());
            credit.setDebt(credit.getDebt() - creditPaySum);
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

    public void changeCreditStatus(Long creditId,CreditStatus creditStatus){
        Credit credit = creditRepository.findById(creditId).orElseThrow(()-> new RuntimeException("Credit not found"));
        credit.setStatus(creditStatus);
        creditRepository.save(credit);
    }


    public List<Credit> getUsersCredits(Long userId) {
        return creditRepository.findAllByUserId(userId);
    }


}
