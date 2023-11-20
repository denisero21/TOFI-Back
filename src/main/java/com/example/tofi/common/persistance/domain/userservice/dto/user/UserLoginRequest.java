package com.example.tofi.common.persistance.domain.userservice.dto.user;

import com.example.tofi.common.persistance.domain.userservice.serialization.EmailDeserialization;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
public class UserLoginRequest implements Serializable {

    @JsonProperty("email")
    @JsonDeserialize(using = EmailDeserialization.class)
    String login;

    @NotBlank(message = "{userLoginRequest.password.notBlank}")
    @JsonProperty("password")
    String password;
}
