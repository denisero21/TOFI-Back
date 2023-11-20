package com.example.tofi.auth.service;


import com.example.tofi.common.exception.PasswordNotMatchedException;
import com.example.tofi.common.persistance.domain.authservice.ResetUserPasswordRequest;
import com.example.tofi.common.persistance.domain.authservice.VerificationToken;
import com.example.tofi.common.persistance.domain.authservice.VerificationTokenType;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.example.tofi.common.persistance.repository.UserRepository;
import com.example.tofi.common.util.MessageUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.tofi.common.persistance.domain.userservice.dto.email.DefaultEmailSubject.RESET_PASSWORD;
import static com.example.tofi.common.persistance.domain.userservice.dto.email.DefaultEmailText.RESET_PASSWORD_TEXT;
import static java.lang.String.format;


@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    private final MessageUtil ms;

    @Value("${spring.mail.username}")
    private String mailSenderUserName;
    @Value("${user.email.url}")
    private String webSiteUrl;

    private static final String VERIFICATION_TOKEN_NOT_VALID = "verificationToken.notValid";
    private static final String VERIFICATION_TOKEN_EXPIRED = "verificationToken.expired";

    @Async
    public void sendResetPasswordEmail(String recipientEmail) {
        User user = getUserByEmail(recipientEmail);
        String expiryToken = verificationTokenService.generateNewExpiryTokenAndDeleteOld(user, VerificationTokenType.RESET_PASSWORD);
        String resetPasswordLink = webSiteUrl + "/reset-password/update?expiryToken=" + expiryToken;
        String emailSubject = RESET_PASSWORD.getSubjectName();
        String emailText = (RESET_PASSWORD_TEXT.getTextName()) + resetPasswordLink;
        emailService.sendSimpleMessage(mailSenderUserName, recipientEmail, emailSubject, emailText);
        //TODO: approve email template
/*      String emailText = format(ms.getMessage
                ("emailText.resetPassword"),user.getFullName(),user.getAgent().getName(),user.getEmail(),user.getPassword(), webSiteUrl
        emailService.sendSimpleMessage(mailSenderUserName, recipientEmail, ms.getMessage("emailSubject.resetPassword"), emailText);*/
    }

    public void resetOldPasswordAndSaveNew(String expiryToken, ResetUserPasswordRequest request) {

        VerificationToken verificationToken = verificationTokenService.findByToken(expiryToken);

        if (!verificationToken.isValid()) {
            throw new EntityNotFoundException(format(ms.getMessage(VERIFICATION_TOKEN_NOT_VALID), verificationToken.getToken()));
        }

        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            verificationTokenService.deleteToken(verificationToken.getId());
            throw new EntityNotFoundException(format(ms.getMessage(VERIFICATION_TOKEN_EXPIRED), verificationToken.getToken()));
        }

        User user = verificationToken.getUser();

        String newPassword = request.getPassword();
        String passwordConfirmation = request.getPasswordConfirmation();

        ResetUserPasswordRequest resetUserPasswordRequest = new ResetUserPasswordRequest();
        resetUserPasswordRequest.setPassword(newPassword);
        resetUserPasswordRequest.setPasswordConfirmation(passwordConfirmation);
        comparePasswords(newPassword, passwordConfirmation);

        saveUserWithNewEncodedPassword(user, newPassword);

        verificationTokenService.deleteToken(verificationToken.getId());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(format(ms.getMessage("user.email.notFound"), email)));
    }

    public void comparePasswords(String password, String anotherPassword) {
        if (!password.equals(anotherPassword)) {
            throw new PasswordNotMatchedException(format(ms.getMessage("userPassword.notMatch"), password, anotherPassword));
        }
    }

    public void saveUserWithNewEncodedPassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}