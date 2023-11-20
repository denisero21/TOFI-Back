package com.example.tofi.common.service;

import com.example.tofi.common.constant.Constant;
import com.example.tofi.common.exception.DataNotFoundException;
import com.example.tofi.common.exception.OtpException;
import com.example.tofi.common.persistance.domain.authservice.LockedUser;
import com.example.tofi.common.persistance.domain.userservice.SmsCode;
import com.example.tofi.common.persistance.repository.SmsCodeRepository;
import com.example.tofi.common.persistance.repository.UserRepository;
import com.example.tofi.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final MessageUtil ms;
    private final SmsCodeRepository smsCodeRepository;
    private final LoginAttemptService loginAttemptService;
    private final UserRepository userRepository;

    public void validateOtp(Integer otpCode, Long userId, String email) {
        SmsCode smsCodeFromDB = smsCodeRepository.findSmsCodesByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException(ms.getMessage("error.otp.not_found")));
        if (!Objects.equals(smsCodeFromDB.getCode(), otpCode)) {
            increaseAttempt(email);
            throw new OtpException(ms.getMessage("error.otp.wrong_code"));
        }
        if (LocalDateTime.now().isAfter(smsCodeFromDB.getExpiryDate())) {
            increaseAttempt(email);
            throw new OtpException(ms.getMessage("error.otp.exp_code"));
        }
        smsCodeRepository.deleteById(smsCodeFromDB.getId());
    }

    private void increaseAttempt(String email) {
        LockedUser user = loginAttemptService.increaseAttempt(email);
        if (user.getUserLocked()) {
            throw new LockedException(
                    ms.getMessage("error.auth.user_locked_until")
                            + " : "
                            + user.getLockTime().format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_PATTERN)));
        }
    }

    public LocalDateTime sendSms(Long userId, String phoneNumber) {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        String login = "login";
        String password = "password";
        String message = String.valueOf(code) /*+ ms.getMessage("info.otp.code")*/; // TODO: 24.07.2023 set message with OTP code
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("smsc.kz")
                    .setPath("/sys/send.php")
                    .addParameter("login", login)
                    .addParameter("password", password)
                    .addParameter("phones", phoneNumber)
                    .addParameter("message", message)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient
                    .newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            int statusCode;
            try {
                statusCode = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
            } catch (InterruptedException e) {
                throw new OtpException(ms.getMessage("error.sms.send"));
            }
            if (statusCode != 200) {
                throw new OtpException(ms.getMessage("error.sms.send"));
            }
        } catch (URISyntaxException | IOException e) {
            throw new OtpException(ms.getMessage("error.sms.send"));
        }

        SmsCode smsCode = new SmsCode();
        smsCode.setUserId(userId);
        smsCode.setCode(code);
        LocalDateTime expiryOtpDate = LocalDateTime.now().plusMinutes(2);
        smsCode.setExpiryDate(expiryOtpDate);
        smsCodeRepository
                .findSmsCodesByUserId(userId)
                .ifPresent(tempCode -> smsCodeRepository.deleteById(tempCode.getId()));
        smsCodeRepository.save(smsCode);
        return expiryOtpDate;
    }

    public LocalDateTime reSendSms(Long userId,String phoneNumber) {
        SmsCode smsCode = smsCodeRepository
                .findSmsCodesByUserId(userId).orElseThrow(() -> new OtpException("error.otp.wrong_code"));
        if(smsCode.getExpiryDate().isAfter(LocalDateTime.now())) throw new OtpException("error.otp.not_exp_code");
        return sendSms(userId,phoneNumber);
    }
}
