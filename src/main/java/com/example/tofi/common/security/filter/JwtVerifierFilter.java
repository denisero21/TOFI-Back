package com.example.tofi.common.security.filter;

import com.example.tofi.common.util.Utilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtVerifierFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        logHeaders(request);

        String username = request.getHeader("username");
        String authoritiesStr = request.getHeader("authorities");
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        if (Utilities.validString(authoritiesStr)) {
            simpleGrantedAuthorities = Arrays
                    .stream(authoritiesStr.split(","))
                    .distinct()
                    .filter(Utilities::validString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        var auth = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
        //SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private void logHeaders(HttpServletRequest httpServletRequest) {
        var headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            log.info(String.format("Header: %s --- Value: %s", header, httpServletRequest.getHeader(header)));
        }
    }
}
