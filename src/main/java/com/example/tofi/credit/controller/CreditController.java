package com.example.tofi.credit.controller;

import com.example.tofi.common.persistance.domain.creditservice.Credit;
import com.example.tofi.common.persistance.domain.creditservice.CreditStatus;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreateCreditDto;
import com.example.tofi.common.persistance.domain.creditservice.dto.CreditPaymentInfoDto;
import com.example.tofi.common.persistance.domain.creditservice.dto.MakePaymentRequest;
import com.example.tofi.credit.service.CreditService;
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
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditController {
    private final CreditService creditService;

    @PostMapping(
            value = "/users/{user_id}/credit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create credit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Credit created"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void createCredit(
            @Valid @RequestBody CreateCreditDto createCreditDto,
            @PathVariable("user_id") Long userId) {
        creditService.createCredit(userId,createCreditDto);
    }

    @GetMapping(
            value = "/users/{user_id}/credit")
    @Operation(summary = "Get list of credits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of credits"),
    })
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public List<Credit> getListCredit(
            @PathVariable("user_id") Long userId) {
        return creditService.getUsersCredits(userId);
    }

    @DeleteMapping(
            value = "/users/{user_id}/credit/{credit_id}")
    @Operation(summary = "Delete credit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete credit"),
    })
//    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void deleteCredit(
            @PathVariable("credit_id") Long creditId) {
         creditService.deleteCredit(creditId);
    }

    @GetMapping(
            value = "/users/{user_id}/credit/{credit_id}")
    @Operation(summary = "Get credit payment info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns credit payment info"),
    })
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public CreditPaymentInfoDto getCreditPaymentInfo(
            @PathVariable("credit_id") Long creditId) {
        return creditService.getCreditPaymentInfo(creditId);
    }

    @PostMapping(
            value = "/users/{user_id}/credit/{credit_id}/pay",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Pay for the credit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all good"),
    })
    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    public void payCredit(
            @PathVariable("credit_id") Long id,
            @RequestBody MakePaymentRequest request) {
         creditService.makePaymentForCredit(id,request);
    }

    @PatchMapping(
            value = "/users/{user_id}/credit/{credit_id}/status")
    @Operation(summary = "Change credit status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed"),
    })
    @PreAuthorize("hasAuthority('OPERATOR_PRIVILEGE')")
    public void changeCreditStatus(
            @PathVariable("credit_id") Long creditId,
            @RequestParam("credit_status") CreditStatus creditStatus) {
         creditService.changeCreditStatus(creditId,creditStatus);
    }
}
