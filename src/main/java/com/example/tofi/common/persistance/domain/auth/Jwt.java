package com.example.tofi.common.persistance.domain.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Jwt {

    Long agentId;
    Long userId;
    String email;
    Boolean isTwoFactor;
    Collection<? extends GrantedAuthority> authorities;
}
