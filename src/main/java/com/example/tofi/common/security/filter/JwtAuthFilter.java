package com.example.tofi.common.security.filter;

import com.example.tofi.common.constant.Constant;
import com.example.tofi.common.persistance.domain.user.User;
import com.example.tofi.common.security.JwtProvider;
import com.example.tofi.common.service.UserDetailsServiceHttpImpl;
import com.example.tofi.common.service.UserDetailsServiceImpl;
import com.example.tofi.common.util.MessageUtil;
import com.example.tofi.common.util.Utilities;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.tofi.common.security.JwtProvider.DEFAULT_OTP_AUTHORITY;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDetailsServiceHttpImpl userDetailsServiceHttp;
    private final MessageUtil ms;

    @Value("${spring.application.name}")
    String appName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth/login")
                || request.getServletPath().contains("/api/reset_password")
                || request.getServletPath().contains("/api/send_email")
                || request.getServletPath().contains("/service")
                || request.getServletPath().contains("/api/banks/sendToEmail")
                || request.getServletPath().contains("/prepare_payment")
                || request.getServletPath().contains("/test_halyk_payment_page")
                || request.getServletPath().contains("/api/banks/p_status")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        boolean isJwtValid;
        try {
            isJwtValid = jwtProvider.isTokenValid(jwt);
        } catch (SignatureException ex) {
            Utilities.produceErrorMessage(response, ms.getMessage("error.jwt.signature"));
            return;
        } catch (MalformedJwtException ex) {
            Utilities.produceErrorMessage(response, ms.getMessage("error.jwt.invalid"));
            return;
        } catch (ExpiredJwtException ex) {
            Utilities.produceErrorMessage(response, ms.getMessage("error.jwt.expired"));
            return;
        } catch (UnsupportedJwtException ex) {
            Utilities.produceErrorMessage(response, ms.getMessage("error.jwt.unsupported"));
            return;
        } catch (IllegalArgumentException ex) {
            Utilities.produceErrorMessage(response, ms.getMessage("error.jwt.claimsIsEmpty"));
            return;
        }

        if (isJwtValid) {
            String userEmail = jwtProvider.extractUsername(jwt);
            Collection<? extends GrantedAuthority> userAuthorities;
            if (Constant.QP_MAIL.equals(userEmail)) {
                userAuthorities = jwtProvider.extractAuthorities(jwt);
            } else {
                User user = null;
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                userAuthorities = userDetails.getAuthorities();

                if (!userDetails.isEnabled()) {
                    Utilities.produceErrorMessage(response, ms.getMessage("error.user.disabled"));
                    return;
                }

                if (checkUserAuthoritiesOnUpdated(jwt, userAuthorities)) {
                    Utilities.produceErrorMessage(response, ms.getMessage("error.authorities_changed"));
                    return;
                }
            }

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        userAuthorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.setAttribute("username", userEmail);
                request.setAttribute("authorities", userAuthorities);
                request.setAttribute("auth-token", jwt);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkUserAuthoritiesOnUpdated(String token, Collection<? extends GrantedAuthority> userAuthorities) {
        Set<?> set1 = new HashSet<>(userAuthorities);
        Set<?> set2 = new HashSet<>(jwtProvider.extractAuthorities(token));
        System.out.println(set2);
        if (set2.toArray()[0].toString().equals(DEFAULT_OTP_AUTHORITY)) {
            return false;
        }
        return !set1.equals(set2);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getRolePrivileges().stream())
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .collect(Collectors.toList());
    }
}