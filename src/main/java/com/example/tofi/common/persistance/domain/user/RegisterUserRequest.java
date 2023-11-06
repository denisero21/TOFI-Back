package com.example.tofi.common.persistance.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserRequest implements Serializable {
    @NotBlank(message = "{user.fullName.notBlank}")
    @Size(max = 90, message = "{user.fullName.size}")
    @JsonProperty("full_name")
    String fullName;

    @NotBlank(message = "{user.email.notBlank}")
    @Size(max = 320, message = "{user.email.size}")
    @JsonProperty("email")
    String email;

    @NotBlank(message = "{user.phoneNumber.notBlank}")
    @Size(min = 12, max = 14, message = "{user.phoneNumber.size}")
    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("is_blocked")
    boolean isBlocked;

    @JsonProperty("two_factor_auth")
    boolean twoFactorAuth;

    private String password;
}
