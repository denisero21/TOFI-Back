package com.example.tofi.deposit.controller;

import com.example.tofi.common.persistance.domain.depositservice.Deposit;
import com.example.tofi.common.persistance.domain.depositservice.dto.CreateDepositDto;
import com.example.tofi.deposit.service.DepositService;
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
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DepositController {
    private final DepositService depositService;

    @PostMapping(
            value = "api/users/{user_id}/deposit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create deposit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deposit created"),
    })
//    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeposit(
            @Valid @RequestBody CreateDepositDto createDepositDto,
            @PathVariable("user_id") Long userId){
        depositService.createDeposit(userId, createDepositDto);
    }

    @PostMapping(
            value = "api/users/{user_id}/deposit/{deposit_id}/close"
    )
    @Operation(summary = "Close deposit")
//    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Account closed"),
    })
    public void closeDeposit(
            @PathVariable("deposit_id") Long depositId){
        depositService.closeDeposit(depositId);
    }

    @GetMapping(
            value = "api/users/{user_id}/deposit")
    @Operation(summary = "Get list of deposits")
//    @PreAuthorize("hasAuthority('CLIENT_PRIVILEGE')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of deposits"),
    })
    public List<Deposit> getUsersDeposits(
            @PathVariable("user_id") Long userId){
        return depositService.getUsersDeposits(userId);
    }
}
