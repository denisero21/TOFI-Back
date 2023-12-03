package com.example.tofi.cryptocurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyService {
    private final RestTemplate restTemplate;
    private final String apiUrl = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH,XMR,ZEC,REP,TRX,USDT,DASH&tsyms=USD,EUR,BYN,RUB,JPY,TRY";

    public String getCryptoCurrencyExchangeData(){
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
