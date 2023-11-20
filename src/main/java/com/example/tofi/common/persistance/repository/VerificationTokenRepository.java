package com.example.tofi.common.persistance.repository;


import com.example.tofi.common.persistance.domain.authservice.VerificationToken;
import com.example.tofi.common.persistance.domain.authservice.VerificationTokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUserIdAndVerificationTokenType(Long userId, VerificationTokenType verificationTokenType);

    Optional<VerificationToken> findByToken(String token);
}
