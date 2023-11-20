package com.example.tofi.common.persistance.domain.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserProfilePasswordRequest implements PasswordConfirmAble, Serializable {

    @NotBlank(message = "{updateUserPasswordRequest.password.notBlank}")
    @JsonProperty("old_password")
    String password;

    @NotBlank(message = "{updateUserPasswordRequest.passwordConfirmation.notBlank}")
    @JsonProperty("new_password")
    String newPassword;

    @NotBlank(message = "{updateUserProfilePasswordRequest.passwordConfirmation.notEmpty}")
    @JsonProperty("password_confirmation")
    String passwordConfirmation;
}
