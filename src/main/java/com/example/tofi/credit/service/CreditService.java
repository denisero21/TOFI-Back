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

import java.math.BigDecimal;
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
    private final SchedulerJobService jobService;

    public void createCredit(Long userId, CreateCreditDto createCreditDto) {
        Account account = accountRepository
                .findById(createCreditDto.getAccountId())
                .orElseThrow(()->new RuntimeException("Account not found"));
        Credit credit = new Credit();
        BeanUtils.copyProperties(createCreditDto, credit);
        credit.setDate(LocalDateTime.now());
        credit.setNextPayDate(LocalDateTime.now().plusMinutes(1));
        credit.setPaymentType(createCreditDto.getPaymentType());
        credit.setUserId(userId);
        credit.setStatus(CreditStatus.APPROVED);
        credit.setPenya(0D);
        credit.setEmailForNotification(createCreditDto.getEmail());
        credit.setPerMonthPaySum(countService.countPerMonthPaySum(
                createCreditDto.getAmountGiven(),
                createCreditDto.getTerm().getTerm(),
                createCreditDto.getTerm().getPercent()));
        credit.setDebt(credit.getPerMonthPaySum() * createCreditDto.getTerm().getTerm());
        credit.setIsNeedManualPayment(!createCreditDto.getPaymentType().equals(PaymentType.AUTO));
        Credit savedCredit = creditRepository.save(credit);
        account.setBalance(BigDecimal.valueOf(credit.getAmountGiven()).add(BigDecimal.valueOf(account.getBalance())).doubleValue());
        accountRepository.save(account);
        if (createCreditDto.getPaymentType().equals(PaymentType.AUTO)) {
            jobService.scheduleNewCredit(savedCredit.getId(), credit);

        }
    }

    public CreditPaymentInfoDto getCreditPaymentInfo(Long id) {
        Credit credit = creditRepository.findById(id).orElseThrow(RuntimeException::new);
        Long days = ChronoUnit.DAYS.between(credit.getNextPayDate(), LocalDateTime.now());
        Double penya = countService.calculatePenalty(credit.getPerMonthPaySum(), days);
        CreditPaymentInfoDto creditPaymentInfoDto = new CreditPaymentInfoDto();
        creditPaymentInfoDto.setCreditId(credit.getId());
        creditPaymentInfoDto.setSumPerMonth(credit.getPerMonthPaySum());
        creditPaymentInfoDto.setPenya(penya);
        creditPaymentInfoDto.setCreditName(credit.getName());
        creditPaymentInfoDto.setDebtAfterPayment(BigDecimal.valueOf(credit.getDebt()).subtract(BigDecimal.valueOf(credit.getPerMonthPaySum())).doubleValue());
        creditPaymentInfoDto.setSumToPay(BigDecimal.valueOf(penya).add(BigDecimal.valueOf(credit.getPerMonthPaySum())).doubleValue());
        return creditPaymentInfoDto;
    }

    public void makePaymentForCredit(Long id, MakePaymentRequest makePaymentRequest) {
        Credit credit = creditRepository.findById(id).orElseThrow(RuntimeException::new);
        if(CreditStatus.PAID.equals(credit.getStatus())){
            throw new RuntimeException("Credit is already paid");
        }
        if (credit.getNextPayDate().toLocalDate().isAfter(LocalDate.now()))
            throw new RuntimeException("Too early to pay");
        Account account = accountRepository.findById(credit.getAccountId()).orElseThrow(()->new RuntimeException("Account not found"));
        if (account.getIsBlocked()) {
            throw new RuntimeException("Operation error.Account is blocked.");
        }
        if (account.getBalance() >= makePaymentRequest.getSumToPay()) {
            account.setBalance(account.getBalance() - makePaymentRequest.getSumToPay());
            credit.setDebt(BigDecimal.valueOf(credit.getDebt()).subtract(BigDecimal.valueOf(credit.getPerMonthPaySum())).doubleValue());
            if(credit.getPaymentType().equals(PaymentType.AUTO)){
                credit.setIsNeedManualPayment(false);
            }
        } else {
            // TODO: 27.11.2023 чет надо сделать
            throw new RuntimeException("Not enough money on bank account ( Иди работай бомжара)");
        }
        if(credit.getDebt()>=0.02 || credit.getDebt()<=-0.02){
            credit.setDebt(0.0);
        }
        if (credit.getDebt().equals(0.0)) {
            credit.setStatus(CreditStatus.PAID);
        } else  {
            if(credit.getPaymentType()!=PaymentType.AUTO){
                credit.setNextPayDate(credit.getNextPayDate().plusMinutes(1));
            }
        }
        creditRepository.save(credit);
        accountRepository.save(account);
    }

    public void changeCreditStatus(Long creditId, CreditStatus creditStatus) {
        Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new RuntimeException("Credit not found"));
        credit.setStatus(creditStatus);
        creditRepository.save(credit);
    }


    public List<Credit> getUsersCredits(Long userId) {
        return creditRepository.findAllByUserIdOrderByDateDesc(userId);
    }

    public void deleteCredit(Long creditId){
        creditRepository.deleteById(creditId);
        jobService.deleteJob(creditId);
    }


}
