package com.example.tofi.common.persistance.domain.userservice.dto.token;

import com.example.tofi.common.constant.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtToken {

    @JsonProperty("token")
    String token;

    @JsonFormat(pattern = Constant.DATE_TIME_PATTERN)
    @JsonProperty("otpExpiration")
    LocalDateTime otpExpiration;
}