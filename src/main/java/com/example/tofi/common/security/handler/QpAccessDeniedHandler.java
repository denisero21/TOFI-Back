package com.example.tofi.common.security.handler;

import com.example.tofi.common.util.MessageUtil;
import com.example.tofi.common.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("accessDeniedHandler")
public class QpAccessDeniedHandler implements AccessDeniedHandler {

    private final MessageUtil ms;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exc) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("User: " + "[" + auth.getName() + "]" + " attempted to access the protected URL: " +
                    request.getRequestURI());
            Utilities.produceErrorMessage(response, ms.getMessage("error.access.denied"));
        }
    }
}