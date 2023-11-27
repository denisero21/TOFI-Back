package com.example.tofi.account.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.service.CountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CountService countService;

    public void createAccount(Long userId, CreateAccountDto createAccountDto){
        Account account = new Account();
        account.setName(createAccountDto.getName());
        account.setCurrency(createAccountDto.getCurrency());
        account.setDate(LocalDateTime.now());
        account.setBalance(0.0);
        account.setIsBlocked(false);
        account.setUserId(userId);
        accountRepository.save(account);
    }

    public void tudaSyudaMillioner(Long accountId){
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + 10000D);
        accountRepository.save(account);
    }

    public List<Account> getUsersAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }


}
