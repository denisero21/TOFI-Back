package com.example.tofi.auth.service;

import com.example.tofi.common.persistance.domain.authservice.VerificationToken;
import com.example.tofi.common.persistance.domain.authservice.VerificationTokenType;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.example.tofi.common.persistance.repository.VerificationTokenRepository;
import com.example.tofi.common.util.MessageUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final MessageUtil ms;

    private static final String VERIFICATION_TOKEN_NOT_FOUND = "verificationToken.notFound";

    @Value("${verificationToken.lifetime.hours}")
    private int expiryTokenLifeTime;

    public String generateNewExpiryTokenAndDeleteOld(User user, VerificationTokenType verificationTokenType) {
        String expiryToken = UUID.randomUUID().toString();

        verificationTokenRepository.findByUserIdAndVerificationTokenType(user.getId(), verificationTokenType).ifPresent(verificationTokenRepository::delete);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(expiryToken);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(expiryTokenLifeTime));
        verificationToken.setUser(user);
        verificationToken.setValid(true);
        verificationToken.setVerificationTokenType(verificationTokenType);
        verificationTokenRepository.save(verificationToken);

        return expiryToken;
    }

    public VerificationToken findByToken(String expiryToken) {
        return verificationTokenRepository.findByToken(expiryToken)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ms.getMessage(VERIFICATION_TOKEN_NOT_FOUND), expiryToken)));
    }

    public void updateToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public void deleteToken(Long id) {
        verificationTokenRepository.deleteById(id);
    }
}