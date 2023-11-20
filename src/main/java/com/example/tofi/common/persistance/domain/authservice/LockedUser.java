package com.example.tofi.common.persistance.domain.authservice;

import com.example.tofi.common.persistance.domain.userservice.AbstractPersistentObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "locked")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LockedUser extends AbstractPersistentObject {

    String email;

    Integer attempt;

    @Column(name = "user_locked")
    Boolean userLocked;

    @Column(name = "lock_time")
    LocalDateTime lockTime;
}
