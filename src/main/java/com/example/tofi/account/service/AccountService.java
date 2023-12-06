package com.example.tofi.account.service;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.ChangeAccountDto;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.domain.accountservice.dto.TransferRequest;
import com.example.tofi.common.persistance.repository.AccountRepository;
import com.example.tofi.common.service.CountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        if(account.getIsBlocked()){
            throw new RuntimeException("Operation error. Account is blocked.");
        }
        account.setBalance(account.getBalance() + 10000D);
        accountRepository.save(account);
    }

    public void burmalda(Long accountId){
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        if(account.getIsBlocked()){
            throw new RuntimeException("Operation error. Account is blocked.");
        }
        account.setBalance(2.15);
        accountRepository.save(account);
    }

    public void getAmountFromDeposit(Long accountId, Double amount){
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        if(account.getIsBlocked()){
            throw new RuntimeException("Operation error. Account is blocked.");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public void makeTransfer(TransferRequest request){
        Account senderAccount = accountRepository
                .findById(request.getSenderId())
                .orElseThrow(()-> new RuntimeException("Sender Account not found"));
        if(senderAccount.getIsBlocked()){
            throw new RuntimeException("Operation error. Sender account is blocked.");
        }

        Account receiverAccount = accountRepository
                .findById(request.getReceiverId())
                .orElseThrow(()-> new RuntimeException("Receiver account not found"));

        if(receiverAccount.getIsBlocked()){
            throw new RuntimeException("Operation error.Receiver Account is blocked.");
        }

        if(senderAccount.getBalance()>= request.getSum()){
            if(receiverAccount.getCurrency().equals(request.getCurrency())){
                senderAccount.setBalance(senderAccount.getBalance()- request.getSum());
                receiverAccount.setBalance(receiverAccount.getBalance()+ request.getSum());
                accountRepository.saveAll(Arrays.asList(senderAccount,receiverAccount));
            }else{
                throw new RuntimeException("Currency of receiver does not match");
            }
        }else{
            throw new RuntimeException("Not enough money on account");
        }
    }

    public void changeAccountStatus(Long accountId, Boolean isBlocked){
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        account.setIsBlocked(isBlocked);
        accountRepository.save(account);
    }

    public void changeAccount(Long accountId, ChangeAccountDto dto){
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));
        account.setName(account.getName());
        accountRepository.save(account);
    }

    public List<Account> getUsersAccounts(Long userId) {
        return accountRepository.findAllByUserIdOrderByDateDesc(userId);
    }


}
