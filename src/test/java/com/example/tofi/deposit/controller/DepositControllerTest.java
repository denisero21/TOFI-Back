package com.example.tofi.deposit.controller;

import com.example.tofi.account.service.AccountService;
import com.example.tofi.common.persistance.domain.accountservice.dto.CreateAccountDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class DepositControllerTest {
    @Mock
    AccountService accountService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    void preparing(){
        setup();
    }

    @Test
    @SneakyThrows
    void createDeposit(){

        var userIds = jdbcTemplate.query("SELECT id FROM tofi_db.public.user_", DataClassRowMapper.newInstance(Long.class));
        log.info("\n>>>>>>>>>>>>>>>> userIds: {}", userIds);
        Long userId = userIds.get(0);
        CreateAccountDto accountDto = new CreateAccountDto();
        accountDto.setName("Arnold's account");
        accountDto.setCurrency("BYN");
        accountService.createAccount(userId, accountDto);
        var accountIds = jdbcTemplate.query("SELECT id FROM tofi_db.public.account WHERE user_id=?", DataClassRowMapper.newInstance(Long.class));
        log.info("\n>>>>>>>>>>>>>>>> accountIds: {}", accountIds);
        Long accountId = accountIds.get(0);


        String request = String.format("""
                {
                  "account_id": %d,
                  "term": "MONTH_3",
                  "amount": 1000,
                  "deposit_type": "REVOCABLE",
                  "name": "string"
                }
                """, accountId);

        mockMvc.perform(
            post("/users/{user_id}/deposit", userId)
                    .header(HttpHeaders.AUTHORIZATION, createAuthHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            )
            .andDo(print())
            .andExpect(status().isCreated());


    }

    @Test
    @SneakyThrows
    void closeDeposit(){

        var userIds = jdbcTemplate.query("SELECT id FROM tofi_db.public.user_", DataClassRowMapper.newInstance(Long.class));
        log.info("\n>>>>>>>>>>>>>>>> userIds: {}", userIds);
        Long userId = userIds.get(0);
        CreateAccountDto accountDto = new CreateAccountDto();
        accountDto.setName("Arnold's account");
        accountDto.setCurrency("BYN");
        accountService.createAccount(userId, accountDto);
        var accountIds = jdbcTemplate.query("SELECT id FROM tofi_db.public.account WHERE user_id=?", DataClassRowMapper.newInstance(Long.class));
        log.info("\n>>>>>>>>>>>>>>>> accountIds: {}", accountIds);
        Long accountId = accountIds.get(0);


        String request1 = String.format("""
                {
                  "account_id": %d,
                  "term": "MONTH_3",
                  "amount": 1000,
                  "deposit_type": "REVOCABLE",
                  "name": "string"
                }
                """, accountId);

        mockMvc.perform(
                post("/users/{user_id}/deposit", userId)
                        .header(HttpHeaders.AUTHORIZATION, createAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request1)
                )
                .andDo(print())
                .andExpect(status().isCreated());


        var depositIds = jdbcTemplate.query("SELECT id FROM tofi_db.public.account WHERE user_id=?", DataClassRowMapper.newInstance(Long.class), userId);
        log.info("\n>>>>>>>>>>>>>>>> accountIds: {}", accountIds);
        Long depositId = depositIds.get(0);

        String request2 = """
                {}
                """;

        mockMvc.perform(
                post("/users/"+userId+"/deposit/{deposit_id}/close", depositId)
                        .header(HttpHeaders.AUTHORIZATION, createAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request2)
                )
                .andDo(print())
                .andExpect(status().isOk());


    }

    String createAuthHeader() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJmdWxsX25hbWUiOiJkZW5pcyIsI…iLmEi9GWtWQrQpNYMptIDw6CRYNq9CktnwaIH_65JhTGuT-aw";
        return  "Bearer " + token;
    }
}