package com.example.tofi.credit.controller;

import com.example.tofi.common.persistance.domain.accountservice.Account;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import com.example.tofi.credit.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;

//    @PostMapping(
//            value = "api/users/{user_id}/credit",
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Create account")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Account created"),
//    })
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createCredit(
//            @Valid @RequestBody CreateAccountDto accountDto,
//            @PathVariable("user_id") Long userId) {
//        creditService.createAccount(userId,accountDto);
//    }

    @GetMapping(
            value = "api/users/{user_id}/credit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create credit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of credits"),
    })
    public List<Account> createCredit(
            @PathVariable("user_id") Long userId) {
        return creditService.getUsersAccounts(userId);
    }
    // TODO: 22.11.2023 Запрос на блокировку
}
