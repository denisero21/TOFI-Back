package com.example.tofi.credit.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final AccountRepository accountRepository;

    public void createCredit(Long userId, CreateCreditDto createCreditDto){
        Credit credit = new Credit();
        BeanUtils.copyProperties(createCreditDto,credit);
        credit.setDate(LocalDateTime.now());
        credit.setDebt(credit.getAmountGiven());
    }

    public List<Account> getUsersAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }


}
