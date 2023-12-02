package com.example.tofi.common.persistance.domain.depositservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "deposit")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDate date;

    @Column(name = "term", nullable = false)
    DepositTerm term;

    @Column(name = "amount", nullable = false)
    Double amount;

    @Column(name = "compensation_amount")
    Double compensationAmount;

    @Column(name = "status")
    DepositStatus status;

    @Column(name = "type")
    DepositType type;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "account_id")
    Long accountId;
}

