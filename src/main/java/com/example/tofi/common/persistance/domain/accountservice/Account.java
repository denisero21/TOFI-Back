package com.example.tofi.common.persistance.domain.accountservice;

import com.example.tofi.common.persistance.domain.userservice.AbstractPersistentObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "account")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AbstractPersistentObject {

    @Column(name = "account_id")
    String accountId;

    @Column(name = "name")
    String name;

    @Column(name = "date")
    LocalDateTime date;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "balance")
    Double balance;

    @Column(name = "currency")
    String currency;

    @Column(name = "is_blocked")
    Boolean isBlocked;
}
