package com.example.tofi.common.persistance.domain.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    String status;
    Boolean isAuthenticated;
    String username;
    String token;
    Collection<GrantedAuthority> authorities;
    String error;
    String[] error_description;
}
