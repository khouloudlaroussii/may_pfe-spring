package com.security.jwt.controller;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.JwtAuthenticationResponse;
import com.security.jwt.dto.LoginRequest;
import com.security.jwt.dto.RegisterRequest;
import com.security.jwt.dto.UserResponseDTO;
import com.security.jwt.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    // Register endpoint that returns UserResponseDTO instead of User
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody RegisterRequest registerRequest) {
        // Use the service response directly
        ApiResponse<UserResponseDTO> response = authenticationService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<JwtAuthenticationResponse> response = authenticationService.login(loginRequest);
        if (response.isErr()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
