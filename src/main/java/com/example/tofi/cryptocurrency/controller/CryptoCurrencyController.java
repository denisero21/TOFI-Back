package com.example.tofi.cryptocurrency.controller;

import com.example.tofi.cryptocurrency.service.CryptoCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class CryptoCurrencyController {
    private final CryptoCurrencyService cryptoCurrencyService;
    @GetMapping(
            value = "/crypto",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list of cryptocurrencies exchange rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of cryptocurrencies exchange rates"),
    })
    public String getCrypto(){
        return cryptoCurrencyService.getCryptoCurrencyExchangeData();
    }
}
