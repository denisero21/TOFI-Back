package com.example.tofi.common.persistance.domain.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto implements Serializable {
    @NotNull
    @JsonProperty("IIN")
    Long iin;

    @Size(max = 90)
    @JsonProperty("full_name")
    String fullName;

    @Size(max = 255)
    @JsonProperty("email")
    String email;

    @Size(min = 13, max = 13)
    @JsonProperty("phone_number")
    String phoneNumber;

    @Size(max = 50)
    @JsonProperty("id")
    Long id;

    @Size(max = 60)
    @JsonProperty("password")
    String password;


    @JsonProperty("role")
    String role;

    @JsonProperty("is_enabled")
    boolean isEnabled;

    @Size(max = 50)
    @JsonProperty("agent_id")
    Long agentId;
}
