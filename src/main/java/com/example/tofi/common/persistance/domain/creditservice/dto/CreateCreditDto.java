package com.example.tofi.common.persistance.domain.creditservice.dto;

import com.example.tofi.common.persistance.domain.creditservice.CreditStatus;
import com.example.tofi.common.persistance.domain.creditservice.CreditTerm;
import com.example.tofi.common.persistance.domain.creditservice.PaymentType;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
