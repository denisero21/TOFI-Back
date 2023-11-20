package com.example.tofi.common.persistance.domain.authservice;

import com.example.tofi.common.persistance.domain.userservice.dto.user.PasswordChangeAble;
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
    @NotBlank(message = "{resetUserPasswordRequest.password.notBlank}")
    @JsonProperty("password")
    String password;
    @NotBlank(message = "{resetUserPasswordRequest.passwordConfirmation.notBlank}")
    @JsonProperty("password_confirmation")
    String passwordConfirmation;
}
