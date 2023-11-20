package com.example.tofi.common.persistance.domain.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest implements NameAble, PhoneAble, Serializable {

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("is_enabled")
    boolean isEnabled;

    @JsonProperty("two_factor_auth")
    boolean twoFactorAuth;

    @JsonProperty("role_ids")
    private Set<Long> roleIds;
}
