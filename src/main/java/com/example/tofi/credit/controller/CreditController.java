package com.example.tofi.credit.controller;

import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.domain.creditservice.dto.MakePaymentRequest;
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

    @PostMapping(
            value = "api/users/{user_id}/credit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    public void createCredit(
            @Valid @RequestBody CreateCreditDto createCreditDto,
            @PathVariable("user_id") Long userId) {
        creditService.createCredit(userId,createCreditDto);
    }

    @GetMapping(
            value = "api/users/{user_id}/credit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list of credits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of credits"),
    })
    public List<Credit> createCredit(
            @PathVariable("user_id") Long userId) {
        return creditService.getUsersCredits(userId);
    }

    @PostMapping(
            value = "api/users/{user_id}/credit/{credit_id}/pay",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Pay for the credit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all good"),
    })
    public void payCredit(
            @PathVariable("credit_id") Long id,
            @RequestBody MakePaymentRequest request) {
         creditService.makePaymentForCredit(id,request);
    }
}
