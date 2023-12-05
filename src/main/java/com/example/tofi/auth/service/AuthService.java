package com.example.tofi.auth.service;

import com.example.tofi.common.constant.Constant;
import com.example.tofi.common.exception.exception.TwoFactorAuthException;
import com.example.tofi.common.persistance.domain.authservice.AuthResponse;
import com.example.tofi.common.persistance.domain.authservice.Jwt;
import com.example.tofi.common.persistance.domain.authservice.LockedUser;
import com.example.tofi.common.persistance.domain.userservice.ConfirmOtpRequest;
import com.example.tofi.common.persistance.domain.userservice.Login;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.example.tofi.common.persistance.domain.userservice.dto.token.JwtToken;
import com.example.tofi.common.persistance.repository.UserRepository;
import com.example.tofi.common.security.JwtProvider;
import com.example.tofi.common.service.LoginAttemptService;
import com.example.tofi.common.service.SmsService;
import com.example.tofi.common.util.MessageUtil;
import com.example.tofi.common.util.Utilities;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MessageUtil ms;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final LoginAttemptService loginAttemptService;
    private final SmsService smsService;

    public JwtToken doLogin(@NotNull Login request, HttpServletRequest servletRequest) {

        try {
            LockedUser lockedUser = loginAttemptService.getUserLocked(request.getLogin());
            if (lockedUser != null && loginAttemptService.isLocked(lockedUser)) {
                throw new LockedException(
                        ms.getMessage("error.auth.user_locked_until")
                                + " : "
                                + lockedUser.getLockTime().format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_PATTERN)));
            }

                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword(),
                                new ArrayList<>()));

                loginAttemptService.deleteLockedUser(request.getLogin());
                final User user = userRepository.findByEmail(request.getLogin()).orElseThrow();

                Jwt jwt = new Jwt();
                jwt.setUserId(user.getId());
                jwt.setEmail(user.getEmail());
                jwt.setFullName(user.getFullName());
                jwt.setAuthorities(Utilities.getUserAuthorities(user));
                jwt.setIpAddress(getIpAddress(servletRequest));

                boolean isTwoFactor = user.isTwoFactorAuth();
                LocalDateTime expiryOtpDate = null;
                if (isTwoFactor) {
                    expiryOtpDate = smsService.sendSms(user.getId(), user.getEmail());
                    jwt.setIsTwoFactor(true);
                } else {
                    jwt.setIsTwoFactor(false);
                }

                jwt.setIsTwoFactor(isTwoFactor);
                return new JwtToken(jwtProvider.generateToken(jwt), expiryOtpDate);
        } catch (BadCredentialsException e) {
            LockedUser user = loginAttemptService.increaseAttempt(request.getLogin());
            if (user.getUserLocked()) {
                throw new LockedException(
                        ms.getMessage("error.auth.user_locked_until")
                                + " : "
                                + user.getLockTime().format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_PATTERN)));
            }
            throw new BadCredentialsException(ms.getMessage("error.auth.bad_credentials"));
        }
    }

    public JwtToken confirmOtp(ConfirmOtpRequest otp, @NotNull HttpServletRequest servletRequest) {

        String jwtAuthHeader = servletRequest
                .getHeader(HttpHeaders.AUTHORIZATION)
                .substring(7);

        Claims jwtClaims = jwtProvider.extractAllClaims(jwtAuthHeader);
        Boolean twoFactorAuth = (Boolean) jwtClaims.get("two_factor");

        if (twoFactorAuth == null || !twoFactorAuth || otp == null) {
            throw new TwoFactorAuthException(ms.getMessage("error.auth.two_factor_disabled"));
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        long userId = Long.parseLong(jwtClaims.get("user_id").toString());
        String fullName = jwtClaims.get("full_name").toString();
        smsService.validateOtp(otp.getOtpCode(), userId, email);

        Jwt jwt = new Jwt();
        jwt.setEmail(email);
        jwt.setUserId(userId);
        jwt.setFullName(fullName);
        jwt.setIsTwoFactor(false);
        jwt.setAuthorities(auth.getAuthorities());
        return new JwtToken(jwtProvider.generateToken(jwt), null);
    }

    public JwtToken refreshOtp(HttpServletRequest request) {
        String jwtAuthHeader = request
                .getHeader(HttpHeaders.AUTHORIZATION)
                .substring(7);
        Claims jwtClaims = jwtProvider.extractAllClaims(jwtAuthHeader);
        long userId = Long.parseLong(jwtClaims.get("user_id").toString());
        final User user = userRepository.findById(userId).orElseThrow();
        LocalDateTime expiryOtpDate = smsService.reSendSms(userId, user.getEmail());
        Jwt jwt = new Jwt();
        jwt.setUserId(user.getId());
        jwt.setEmail(user.getEmail());
        jwt.setFullName(user.getFullName());
        jwt.setAuthorities(Utilities.getUserAuthorities(user));
        return new JwtToken(jwtProvider.generateToken(jwt), expiryOtpDate);
    }

    public AuthResponse validateToken(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String token = (String) request.getAttribute("auth-token");
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> grantedAuthorities =
                (Collection<GrantedAuthority>) request.getAttribute("authorities");
        if (grantedAuthorities == null || grantedAuthorities.isEmpty()) {
            log.error("The user '{}' does not have any assigned privileges", username);
        }
        return AuthResponse
                .builder()
                .status("OK")
                .username(username)
                .token(token)
                .authorities(grantedAuthorities)
                .isAuthenticated(true)
                .build();
    }

    private String getIpAddress(@NotNull HttpServletRequest req) {
        String ip;
        String xForwardedForHeader = req.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            ip = req.getRemoteAddr();
        } else {
            ip = new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
        return ip;
    }
}
