package com.example.tofi.common.persistance.domain.userservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "sms_code")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmsCode extends AbstractPersistentObject{

    @Column(name = "user_id")
    Long userId;

    @Column(name = "expiry_date")
    LocalDateTime expiryDate;

    @Column(name = "code")
    Integer code;
}
