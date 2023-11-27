package com.example.tofi.common.persistance.domain.creditservice;

import com.example.tofi.common.persistance.domain.userservice.AbstractPersistentObject;
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
    @Column(name = "credit_id")
    Long creditId;

    @Column(name = "date")
    LocalDateTime date;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "account_id")
    Long accountId;

    @Column(name = "term")
    CreditTerm term;

    @Column(name = "amount_given")
    Double amountGiven;

    @Column(name = "debt")
    Double debt;

    @Column(name = "next_pay_date")
    LocalDateTime nextPayDate;

    @Column(name = "per_month_pay_sum")
    Double perMonthPaySum;

    @Column(name = "status")
    CreditStatus status;

    @Column(name = "payment_type")
    PaymentType paymentType;

    @Column(name = "is_notification_enabled")
    Boolean isNotificationEnabled;
}
