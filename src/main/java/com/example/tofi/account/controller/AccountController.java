package com.example.tofi.account.controller;

import com.example.tofi.account.service.AccountService;
import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.ChangeAccountDto;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.common.persistance.domain.accountservice.dto.TransferRequest;
import com.example.tofi.common.persistance.domain.creditservice.CreditStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void createAccount(
            @Valid @RequestBody CreateAccountDto accountDto,
            @PathVariable("user_id") Long userId) {
        accountService.createAccount(userId,accountDto);
    }

    @GetMapping(
            value = "api/users/{user_id}/accounts"
    )
    @Operation(summary = "Get list of accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of accounts"),
    })
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public List<Account> createAccount(
            @PathVariable("user_id") Long userId) {
        return accountService.getUsersAccounts(userId);
    }

    @GetMapping(
            value = "api/users/{user_id}/accounts/{account_id}/add_money",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Temki zhestkie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kalyan pokuren"),
    })
    public void addMoney(
            @PathVariable("account_id") Long id) {
        accountService.tudaSyudaMillioner(id);
    }

    @GetMapping(
            value = "api/users/{user_id}/accounts/{account_id}/no_money",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Make ur account have 2.15 dollars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bonuska ne vipala"),
    })
    public void deleteMoney(
            @PathVariable("account_id") Long id) {
        accountService.burmalda(id);
    }

    @PostMapping(
            value = "api/users/{user_id}/accounts/transfer",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Make transfer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success transfer"),
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void makeTransfer(
            @Valid @RequestBody TransferRequest request) {
        accountService.makeTransfer(request);
    }

    @PatchMapping(
            value = "api/users/{user_id}/account/{account_id}/status")
    @Operation(summary = "Change account status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed"),
    })
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void changeAccountStatus(
            @PathVariable("account_id") Long accountId,
            @RequestParam("is_blocked") Boolean isBlocked) {
        accountService.changeAccountStatus(accountId,isBlocked);
    }

    @PutMapping(
            value = "api/users/{user_id}/account/{account_id}")
    @Operation(summary = "Change account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account changed"),
    })
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void changeAccount(
            @PathVariable("account_id") Long accountId,
            @RequestBody ChangeAccountDto dto) {
        accountService.changeAccount(accountId,dto);
    }
}
