package com.example.tofi.common.persistance.domain.accountservice;

import com.example.tofi.common.persistance.domain.userservice.AbstractPersistentObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @Column(name = "name")
    String name;

    @Column(name = "date")
    LocalDateTime date;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    Long userId;

    @JsonProperty("balance")
    @Column(name = "balance")
    Double balance;

    @JsonProperty("currency")
    @Column(name = "currency")
    String currency;

    @JsonProperty("is_blocked")
    @Column(name = "is_blocked")
    Boolean isBlocked;
}
