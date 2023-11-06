package com.example.tofi.common.service;


import com.example.tofi.common.persistance.domain.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceHttpImpl {

    private static final String USER_SERVICE_URL = "http://USER-SERVICE/api/users?email={email}";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    public User loadUserByUsername(HttpServletRequest request, String email) throws UsernameNotFoundException {
        try {
            final var user = sendRequestForUser(request, email);
            if (user != null) {
                return user;
            } else {
                throw new UsernameNotFoundException("User Not Found!");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User sendRequestForUser(HttpServletRequest request, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String userJson = restTemplate.exchange(USER_SERVICE_URL,
                HttpMethod.GET,
                entity,
                String.class,
                email
        ).getBody();
        try {
            return mapper.readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
