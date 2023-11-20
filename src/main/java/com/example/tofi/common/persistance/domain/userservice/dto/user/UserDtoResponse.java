package com.example.tofi.common.persistance.domain.userservice.dto.user;

import com.example.tofi.common.constant.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoResponse implements Serializable {

    @JsonProperty("id")
    Long id;

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("is_enabled")
    boolean isEnabled;

    @JsonProperty("two_factor_auth")
    boolean twoFactorAuth;

    @JsonProperty("email")
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("role_name")
    String roleName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_PATTERN)
    @Column(name = "date")
    LocalDateTime date;
}
