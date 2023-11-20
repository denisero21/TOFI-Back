package com.example.tofi.common.persistance.domain.userservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmOtpRequest {

    @NotNull(message = "{confirmOtpRequest.otpCode.notNull}")
    @JsonProperty("otp_code")
    Integer otpCode;
}
