package com.security.jwt.service.impl;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.JwtAuthenticationResponse;
import com.security.jwt.dto.LoginRequest;
import com.security.jwt.dto.RegisterRequest;
import com.security.jwt.dto.UserResponseDTO;
import com.security.jwt.model.Role;
import com.security.jwt.model.User;
import com.security.jwt.repository.IUserRepository;
import com.security.jwt.service.IAuthenticationService;
import com.security.jwt.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    @Override
    public ApiResponse<UserResponseDTO> register(RegisterRequest registerRequest) {
        // Check if the user already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return new ApiResponse<>(true, "Email already registered", null);
        }

        // Create and save a new user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setBirthday(registerRequest.getBirthday());
        user.setPhone(registerRequest.getPhone());
        user.setAddress(registerRequest.getAddress());
        user.setImagePath(registerRequest.getImagePath());
        user.setStatus(1);

        User savedUser = userRepository.save(user);

        // Convert User entity to UserResponseDTO
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(savedUser.getId());
        userResponseDTO.setFirstName(savedUser.getFirstName());
        userResponseDTO.setLastName(savedUser.getLastName());
        userResponseDTO.setEmail(savedUser.getEmail());
        userResponseDTO.setRole(savedUser.getRole());
        userResponseDTO.setBirthday(savedUser.getBirthday());
        userResponseDTO.setAddress(savedUser.getAddress());
        userResponseDTO.setStatus(0);
        userResponseDTO.setPhone(savedUser.getPhone());
        userResponseDTO.setImagePath(savedUser.getImagePath());

        return new ApiResponse<>(false, "User registered successfully!", userResponseDTO);
    }

    @Override
    public ApiResponse<JwtAuthenticationResponse> login(LoginRequest loginRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            // Return an error response if authentication fails
            return new ApiResponse<>(true, "Invalid email or password", null);
        }

        // Fetch the user from the database
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if(user.getStatus() == 0){
            return new ApiResponse<>(true, "Login failed !", null);

        }
        // Generate a JWT token with user's ID and email
        String jwt = jwtService.generateToken(user, user.getId(), user.getEmail(),user.getRole(),user.getFirstName(),user.getLastName(),user.getImagePath(),user.getConnectionOtp());
        // Create a response object
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setId(user.getId());
        response.setToken(jwt);
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole().name());
        response.setImagePath(user.getImagePath());
        response.setConnectionOtp(user.getConnectionOtp());
//        response.setStatus(user.getStatus());

        // Return a success response

            return new ApiResponse<>(false, "Login successful!", response);


    }

}
