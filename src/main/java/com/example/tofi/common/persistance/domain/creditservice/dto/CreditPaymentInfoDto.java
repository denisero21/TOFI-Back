package com.example.tofi.common.persistance.domain.creditservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreditPaymentInfoDto {

    @JsonProperty("credit_name")
    String creditName;

    @JsonProperty("credit_id")
    Long creditId;

    @JsonProperty("sum")
    Double sum;

    @JsonProperty("penya")
    Double penya;

    @JsonProperty("debt_after_payment")
    Double debtAfterPayment;

}
