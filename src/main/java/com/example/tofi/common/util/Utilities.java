package com.example.tofi.common.util;

import com.example.tofi.common.exception.ErrorInfo;
import com.example.tofi.common.persistance.domain.userservice.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class Utilities {

    public static void produceErrorMessage(HttpServletResponse resp, String errorMessage) {
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE + "; charset=" + StandardCharsets.UTF_8);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setError(errorMessage);
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorInfo));
            out.close();
        } catch (IOException e) {
            if (out != null) {
                out.close();
            }
            throw new RuntimeException(e);
        }
    }

    public static Collection<? extends GrantedAuthority> getUserAuthorities(@NotNull User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getRolePrivileges().stream())
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .collect(Collectors.toList());
    }

    public static boolean validString(String input) {
        return input != null && !input.isEmpty();
    }
}
