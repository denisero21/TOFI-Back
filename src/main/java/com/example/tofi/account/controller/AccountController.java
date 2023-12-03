package com.example.tofi.account.controller;

import com.example.tofi.account.service.AccountService;
import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping(
            value = "api/users/{user_id}/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(
            @Valid @RequestBody CreateAccountDto accountDto,
            @PathVariable("user_id") Long userId) {
        accountService.createAccount(userId,accountDto);
    }

    @GetMapping(
            value = "api/users/{user_id}/accounts"
    )
    @Operation(summary = "Create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of accounts"),
    })
    public List<Account> createAccount(
            @PathVariable("user_id") Long userId) {
        return accountService.getUsersAccounts(userId);
    }

    @GetMapping(
            value = "api/users/{user_id}/accounts/{account_id}/tuda_syuda_millioner",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Temki zhestkie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kalyan pokuren"),
    })
    public void addMoney(
            @PathVariable("account_id") Long id) {
        accountService.tudaSyudaMillioner(id);
    }


    // TODO: 22.11.2023 Запрос на блокировку
}
