package com.example.tofi.common.persistance.domain.creditservice.dto;

import com.example.tofi.common.persistance.domain.creditservice.CreditTerm;
import com.example.tofi.common.persistance.domain.creditservice.PaymentType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCreditDto {

    Long accountId;

    CreditTerm term;

    PaymentType paymentType;

    Double amountGiven;

    Boolean isNotificationEnabled;
}
