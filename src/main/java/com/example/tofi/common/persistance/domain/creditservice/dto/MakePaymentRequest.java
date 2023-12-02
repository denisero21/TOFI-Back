package com.example.tofi.common.persistance.domain.creditservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MakePaymentRequest {

    @JsonProperty("sum_to_pay")
    Double sumToPay;
}
