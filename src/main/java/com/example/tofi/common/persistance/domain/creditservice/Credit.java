package com.example.tofi.common.persistance.domain.creditservice;

import com.example.tofi.common.persistance.domain.userservice.AbstractPersistentObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "credit")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Credit extends AbstractPersistentObject {

    String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "date")
    LocalDateTime date;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    Long userId;

    @JsonProperty("account_id")
    @Column(name = "account_id")
    Long accountId;

    @JsonProperty("term")
    @Column(name = "term")
    CreditTerm term;

    @JsonProperty("amount_given")
    @Column(name = "amount_given")
    Double amountGiven;

    @JsonProperty("debt")
    @Column(name = "debt")
    Double debt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("next_pay_date")
    @Column(name = "next_pay_date")
    LocalDateTime nextPayDate;

    @JsonProperty("per_month_pay_sum")
    @Column(name = "per_month_pay_sum")
    Double perMonthPaySum;

    @JsonProperty("penya")
    @Column(name = "penya")
    Double penya;

    @JsonProperty("status")
    @Column(name = "status")
    CreditStatus status;

    @JsonProperty("payment_type")
    @Column(name = "payment_type")
    PaymentType paymentType;

    @JsonProperty("is_notification_enabled")
    @Column(name = "is_notification_enabled")
    Boolean isNotificationEnabled;
}
