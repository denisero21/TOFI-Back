package com.example.tofi.credit.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.persistance.repository.CreditRepository;
import com.example.tofi.common.service.CountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository repository;
    private final CountService countService;

    public void createCredit(Long userId, CreateCreditDto createCreditDto){
        Credit credit = new Credit();
        BeanUtils.copyProperties(createCreditDto,credit);
        credit.setDate(LocalDateTime.now());
        credit.setDebt(credit.getAmountGiven());
        credit.setNextPayDate(LocalDateTime.now().plusMonths(createCreditDto.getTerm().getTerm()));
        credit.setUserId(userId);
        credit.setPerMonthPaySum(countService.countPerMonthPaySum(
                createCreditDto.getAmountGiven(),
                createCreditDto.getTerm().getTerm(),
                createCreditDto.getTerm().getPercent()));
        repository.save(credit);
    }



    public List<Credit> getUsersCredits(Long userId) {
        return repository.findAllByUserId(userId);
    }



}
