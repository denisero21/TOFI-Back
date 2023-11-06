package com.example.tofi.common.persistance.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetUserPasswordRequest implements PasswordChangeAble {
    @NotBlank
    @JsonProperty("password")
    String password;
    @NotBlank
    @JsonProperty("password_confirmation")
    String passwordConfirmation;
}
