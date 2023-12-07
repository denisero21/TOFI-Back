package com.example.tofi.auth.controller;

import com.example.tofi.auth.service.AuthService;
import com.example.tofi.common.persistance.domain.authservice.AuthResponse;
import com.example.tofi.common.persistance.domain.userservice.ConfirmOtpRequest;
import com.example.tofi.common.persistance.domain.userservice.Login;
import com.example.tofi.common.persistance.domain.userservice.dto.token.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;




@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "/api/auth",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return JWT token. If two-factor authentication returns." +
                    "a temporary token and sends an SMS code to the phone number stored in the database."),
            @ApiResponse(responseCode = "401", description = "Credentials entered incorrectly.")
    })
    @CrossOrigin("https://tofi-bank.netlify.app")
    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    JwtToken authenticate(@RequestBody Login request,
                          HttpServletRequest servletRequest) {
        return authService.doLogin(request, servletRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMS code has been validated."),
            @ApiResponse(responseCode = "401", description = "SMS code has expired or SMS invalid.")
    })
    @Operation(summary = "Confirm SMS code.")
    @PostMapping(
            value = "/confirm_otp",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    JwtToken confirmOtp(@Valid @RequestBody ConfirmOtpRequest request, HttpServletRequest servletRequest) {
        return authService.confirmOtp(request, servletRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMS code sent."),
    })
    @Operation(summary = "Refresh SMS code.")
    @GetMapping(value = "/refresh_otp")
    @SecurityRequirement(name = "bearerAuth")
    JwtToken refreshOtp(HttpServletRequest request) {
        return authService.refreshOtp(request);
    }

    @Operation(summary = "Validate JWT token.", hidden = true)
    @GetMapping(
            value = "/validateToken",
            produces = MediaType.APPLICATION_JSON_VALUE)
    AuthResponse validateToken(HttpServletRequest request) {
        return authService.validateToken(request);
    }
}