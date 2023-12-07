package com.example.tofi.auth.controller;

import com.example.tofi.auth.service.PasswordService;
import com.example.tofi.common.persistance.domain.authservice.ResetUserPasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ResetPasswordController {
    private final PasswordService passwordService;

    @GetMapping(value = "/send_email")
    @Operation(summary = "Send email with reset password link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email with reset password link sent")
    })
    void sendEmailWithExpiryToken(@RequestParam("email") String email) {
        passwordService.sendResetPasswordEmail(email);
    }

    @PostMapping(
            value = "/reset_password/update",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Reset and update user password using link from email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Password updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    void setNewPasswordUsingLink(@RequestParam("expiryToken") String expiryToken,
                                 @Valid @RequestBody ResetUserPasswordRequest resetUserPasswordRequest) {
        passwordService.resetOldPasswordAndSaveNew(expiryToken, resetUserPasswordRequest);
    }
}