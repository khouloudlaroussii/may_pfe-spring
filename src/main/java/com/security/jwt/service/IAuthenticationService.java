package com.security.jwt.service;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.JwtAuthenticationResponse;
import com.security.jwt.dto.LoginRequest;
import com.security.jwt.dto.RegisterRequest;
import com.security.jwt.dto.UserResponseDTO;

public interface IAuthenticationService {
    ApiResponse<UserResponseDTO> register(RegisterRequest registerRequest);  // Return UserResponseDTO instead of User
    ApiResponse<JwtAuthenticationResponse> login(LoginRequest loginRequest);
}
