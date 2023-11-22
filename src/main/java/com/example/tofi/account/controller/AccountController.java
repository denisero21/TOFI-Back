package com.example.tofi.account.controller;

import com.example.tofi.account.service.AccountService;
import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.domain.userservice.RegisterUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            value = "api/users/{user_id}/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of accounts"),
    })
    public List<Account> createAccount(
            @PathVariable("user_id") Long userId) {
        return accountService.getUsersAccounts(userId);
    }
    // TODO: 22.11.2023 Запрос на блокировку
}