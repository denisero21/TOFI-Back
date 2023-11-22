package com.example.tofi.account.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void createAccount(Long userId, CreateAccountDto createAccountDto){
        Account account = new Account();
        account.setCurrency(createAccountDto.getCurrency());
        account.setDate(LocalDateTime.now());
        account.setBalance(0.0);
        account.setIsBlocked(false);
        account.setUserId(userId);
        accountRepository.save(account);
    }

    public List<Account> getUsersAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }


}
