package com.example.tofi.user.controller;

import com.example.tofi.common.persistance.domain.user.RegisterUserRequest;
import com.example.tofi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping(
            value = "api/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
    })
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest userDto) {
        userService.register(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
    })
    public String get() {
        return "KIRILL LOH";
    }
}
