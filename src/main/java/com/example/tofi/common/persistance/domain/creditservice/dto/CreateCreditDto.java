package com.example.tofi.common.persistance.domain.creditservice.dto;

import com.example.tofi.common.persistance.domain.creditservice.CreditTerm;
import com.example.tofi.common.persistance.domain.creditservice.PaymentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCreditDto {

    @JsonProperty("account_id")
    Long accountId;

    String name;

    @JsonProperty("term")
    CreditTerm term;

    @JsonProperty("payment_type")
    PaymentType paymentType;

    @JsonProperty("amount_given")
    Double amountGiven;

    @JsonProperty("is_notification_enabled")
    Boolean isNotificationEnabled;

    String email;
}
