package com.example.tofi.common.persistance.domain.creditservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreditPaymentInfoDto {

    String creditName;

    Long creditId;

    Double sum;

    Double penya;

    Double debtAfterPayment;

}
