package com.example.tofi.common.persistance.domain.userservice;

import com.example.tofi.common.persistance.domain.userservice.dto.user.PasswordAble;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Login implements PasswordAble {

    @Email(message = "{login.login.email}", regexp = "(?=^.{1,322}$)^([a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]{1,64})@([a-zA-Z0-9-]+[.a-zA-Z0-9-]{1,253}){2,}$")
    private String login;

    private String password;
}
