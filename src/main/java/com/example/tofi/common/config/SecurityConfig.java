package com.example.tofi.common.config;


import com.example.tofi.common.security.filter.JwtAuthFilter;
import com.example.tofi.common.security.filter.JwtVerifierFilter;
import com.example.tofi.common.security.handler.QpAccessDeniedHandler;
import com.example.tofi.common.security.handler.QpAuthenticationEntryPoint;
import com.example.tofi.common.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtVerifierFilter jwtVerifierFilter;
    private final JwtAuthFilter jwtAuthFilter;
    private final QpAccessDeniedHandler qpAccessDeniedHandler;
    private final QpAuthenticationEntryPoint qpAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .cors()

                .and()
                .authorizeHttpRequests(auth -> {
                            try {
                                auth
                                        .requestMatchers(
                                                "/v3/**",
                                                "/swagger-resources/**",
                                                "/swagger-ui.html",
                                                "/swagger-ui/**",
                                                "/api-docs/**",
                                                "/configuration/ui/**",
                                                "/configuration/security/**",
                                                "/webjars/**",
                                                "/auth-service/v3/api-docs/**",
                                                "/user-service/v3/api-docs/**",
                                                "/paylink-service/v3/api-docs/**",
                                                "/virtual-pos-service/v3/api-docs/**",
                                                "/transaction-service/v3/api-docs/**",
                                                "/support-service/v3/api-docs/**",
                                                "/bank-router-service/v3/api-docs/**",
                                                "/bank-halyk/v3/api-docs/**",
                                                "/bank-forte/v3/api-docs/**",
                                                "/bank-bereke/v3/api-docs/**",
                                                "/card-service/v3/api-docs/**",
                                                "/support-service/v3/api-docs/**",
                                                "/notification-service/v3/api-docs/**",
                                                "/reference-book/v3/api-docs/**",
                                                "/analytics-service/v3/api-docs/**",
                                                "/audit-service/v3/api-docs/**",
                                                "/service/**",
                                                "/static/**",
                                                "/test_halyk_payment_page",
                                                "/api/banks/sendToEmail",
                                                "/service/agent_payment_page",
                                                "/service/payment_page")
                                        .permitAll()

                                        .requestMatchers("/api/auth/login", "/api/auth/logout","api/auth/register","api/**")
                                        .permitAll()

                                        .requestMatchers("/api/send_email/**")
                                        .permitAll()

                                        .requestMatchers("/api/reset_password/**")
                                        .permitAll()

                                        .requestMatchers(
                                                "/api/auth/confirm_otp",
                                                "/api/auth/refresh_otp",
                                                "/api/auth/validateToken")
                                        .permitAll()

                                        .requestMatchers("/api/agents/**", "/api/users/**", "/roles/**")
                                        .permitAll()

                                        .requestMatchers("/**")
                                        .permitAll()

                                        .and()
                                        .exceptionHandling(exc -> exc
                                                .accessDeniedHandler(qpAccessDeniedHandler)
                                                .authenticationEntryPoint(qpAuthenticationEntryPoint)
                                        );
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtVerifierFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }
}