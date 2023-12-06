package com.example.tofi.common.persistance.domain.depositservice.dto;

import com.example.tofi.common.persistance.domain.depositservice.DepositTerm;
import com.example.tofi.common.persistance.domain.depositservice.DepositType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDepositDto {
    @JsonProperty("account_id")
    Long accountId;

    @JsonProperty("term")
    DepositTerm term;

    @JsonProperty("amount")
    Double amount;

    @JsonProperty("deposit_type")
    DepositType type;

    @JsonProperty("name")
    String name;
}
