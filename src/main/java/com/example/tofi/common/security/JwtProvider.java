package com.example.tofi.common.security;

import com.example.tofi.common.persistance.domain.authservice.Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class JwtProvider {

    @Value("${encrypt.key}")
    private String secret;
    public static final String DEFAULT_OTP_AUTHORITY = "CONFIRM_OTP";
    public static final String PAYMENT_AUTHORITY = "PAYMENT_AUTHORITY";
    public static final String ISSUER_ID = "QAZAQPAY";
    public static final String SUBJECT = "QP";
    private static final int PAYMENT_TOKEN_EXPIRATION = 1000 * 60 * 20; //15m
    private static final int JWT_TOKEN_EXPIRATION = 1000 * 60 * 60; //1h
    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("email") == null ? null : claims.get("email").toString();
    }

    public <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private @NotNull Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(@NotNull Jwt jwt) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", "JWT");
        claims.put("agent_id", jwt.getAgentId());
        claims.put("user_id", jwt.getUserId());
        claims.put("email", jwt.getEmail());
        claims.put("two_factor", jwt.getIsTwoFactor());

        if (!jwt.getIsTwoFactor()) {
            claims.put("authorities", jwt.getAuthorities());
        } else {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add((GrantedAuthority) () -> DEFAULT_OTP_AUTHORITY);
            claims.put("authorities", authorities);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(SUBJECT)
                .setIssuer(ISSUER_ID)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractAllClaims(token);
        String jwtIssuer = claims.getIssuer();
        String jwtSubject = claims.getSubject();
        Object oIsPaymentLink = claims.get("isPaymentLink");
        if (oIsPaymentLink != null) {
            boolean isPaymentLink = Boolean.parseBoolean(oIsPaymentLink.toString());
            if (isPaymentLink) {
                return (ISSUER_ID.equals(jwtIssuer)) &&
                        (SUBJECT.equals(jwtSubject));
            }
        }
        return (ISSUER_ID.equals(jwtIssuer)) &&
                (SUBJECT.equals(jwtSubject)) &&
                !isTokenExpired(token);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        @SuppressWarnings("unchecked")
        List<Map<String, String>> privilegeList = claims.get("authorities", List.class);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Map<String, String> privilegeMap : privilegeList) {
            grantedAuthorities.add(new SimpleGrantedAuthority(privilegeMap.get("authority")));
        }
        return grantedAuthorities;
    }
}