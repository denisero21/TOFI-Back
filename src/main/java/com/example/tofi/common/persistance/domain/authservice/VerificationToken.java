package com.example.tofi.common.persistance.domain.authservice;

import com.example.tofi.common.persistance.domain.userservice.AbstractPersistentObject;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "user_verification_token")
public class VerificationToken extends AbstractPersistentObject {

    @Column(name = "token")
    private String token;

    @Column(name = "expire_at")
    private LocalDateTime expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "valid")
    private boolean isValid;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private VerificationTokenType verificationTokenType;
}
