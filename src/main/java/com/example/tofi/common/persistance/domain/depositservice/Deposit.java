package com.example.tofi.common.persistance.domain.depositservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.time.LocalDateTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime date;

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

    @Column(name = "end_date")
    LocalDateTime endDate;
}

