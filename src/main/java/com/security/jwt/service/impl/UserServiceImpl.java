package com.security.jwt.service.impl;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.JwtAuthenticationResponse;
import com.security.jwt.dto.RoleCountDto;
import com.security.jwt.model.Role;
import com.security.jwt.model.User;
import com.security.jwt.repository.IUserRepository;
import com.security.jwt.service.EmailService;
import com.security.jwt.service.IJwtService;
import com.security.jwt.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final EmailService emailService; // Assuming you have an EmailService to handle email sending
    private final IJwtService jwtService;
    private static final int OTP_LENGTH = 6;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public ApiResponse<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return new ApiResponse<>(true, "No users found", null);
            }
            return new ApiResponse<>(false, "Users retrieved successfully!", users);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving users.", null);
        }
    }

    @Override
    public ApiResponse<List<User>> getUsersByRole(Role role) {
        try {
            List<User> users = userRepository.findByRole(role);
            if (users.isEmpty()) {
                return new ApiResponse<>(true, "No users found with role " + role, null);
            }
            return new ApiResponse<>(false, "Users with role " + role + " retrieved successfully!", users);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving users.", null);
        }
    }

    @Transactional
    @Override
    public ApiResponse<User> updateUser(int id, User userUpdates) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (userUpdates.getFirstName() != null) {
                user.setFirstName(userUpdates.getFirstName());
            }
            if (userUpdates.getLastName() != null) {
                user.setLastName(userUpdates.getLastName());
            }
            if (userUpdates.getEmail() != null) {
                user.setEmail(userUpdates.getEmail());
            }
            if (userUpdates.getPhone() != null) {
                user.setPhone(userUpdates.getPhone());
            }
            if (userUpdates.getAddress() != null) {
                user.setAddress(userUpdates.getAddress());
            }
            if (userUpdates.getStatus() != null) {
                user.setStatus(userUpdates.getStatus());
            }
            if (userUpdates.getImagePath() != null) {
                user.setImagePath(userUpdates.getImagePath());
            }

            userRepository.save(user);
            return new ApiResponse<>(false, "User updated successfully!", user);
        } else {
            return new ApiResponse<>(true, "User not found", null);
        }
    }

    @Override
    public ApiResponse<User> getUserById(Integer id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
            return new ApiResponse<>(false, "User retrieved successfully!", user);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving the user.", null);
        }
    }

    @Override
    public ApiResponse<List<RoleCountDto>> getUserCountsForAllRoles() {
        try {
            List<RoleCountDto> roleCounts = List.of(
                    new RoleCountDto(Role.ADMIN, userRepository.countByRole(Role.ADMIN)),
                    new RoleCountDto(Role.USER, userRepository.countByRole(Role.USER)),
                    new RoleCountDto(Role.DELIVERYMAN, userRepository.countByRole(Role.DELIVERYMAN))
            );

            return new ApiResponse<>(false, "User counts for all roles retrieved successfully!", roleCounts);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving role counts.", null);
        }
    }

    @Override
    public ApiResponse<String> sendOtpToUser(int userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            // Generate OTP
            String otp = generateOtp();
            user.setOtp(otp);
            userRepository.save(user);

            // Log email sending
            System.out.println("Sending OTP to email: " + user.getEmail());
            String emailMessage = "Your OTP code is: " + otp;
            emailService.sendEmail(user.getEmail(), "OTP Verification", emailMessage);

            return new ApiResponse<>(false, "OTP sent successfully to " + user.getEmail(), null);
        } catch (Exception ex) {
            System.err.println("An error occurred while sending OTP: " + ex.getMessage());
            ex.printStackTrace();
            return new ApiResponse<>(true, "An unexpected error occurred while sending OTP.", null);
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    @Override
    public ApiResponse<String> verifyOtp(int userId, String otp) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                return new ApiResponse<>(true, "User not found with ID: " + userId, null);
            }

            User user = userOptional.get();

            // Check if the OTP matches
            if (otp.equals(user.getOtp())) {
                user.setConnectionOtp(true);
                user.setOtp(null);
                userRepository.save(user);
                // Generate a JWT token with user's ID and email
                String jwt = jwtService.generateToken(user, user.getId(), user.getEmail(),user.getRole(),user.getFirstName(),user.getLastName(),user.getImagePath(),user.getConnectionOtp());
                return new ApiResponse<>(false, "OTP verified successfully!", jwt);
            } else {
                return new ApiResponse<>(true, "Invalid OTP provided.", null);
            }
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred while verifying OTP.", null);
        }
    }
}
