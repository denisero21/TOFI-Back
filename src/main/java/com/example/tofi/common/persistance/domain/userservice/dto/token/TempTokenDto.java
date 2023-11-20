package com.example.tofi.common.persistance.domain.userservice.dto.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TempTokenDto {
    @JsonProperty("typ")
    String tokenType;
    @JsonProperty("exp")
    String expiryTime;
    @JsonProperty("user_id")
    String userId;
    @Size(max = 50)
    @JsonProperty("agent_id")
    String agentId;
    @Size(max = 320)
    @JsonProperty("email")
    String email;
    @Size(max = 160)
    @JsonProperty("privileges")
    PrivilegeHolder privilegeHolder;
}
