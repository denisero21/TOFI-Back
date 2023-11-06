package com.example.tofi.common.security.handler;

import com.example.tofi.common.util.MessageUtil;
import com.example.tofi.common.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QpAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private final MessageUtil ms;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Utilities.produceErrorMessage(response, ms.getMessage("error.user.authorize"));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("QAZAQPAY");
        super.afterPropertiesSet();
    }
}
