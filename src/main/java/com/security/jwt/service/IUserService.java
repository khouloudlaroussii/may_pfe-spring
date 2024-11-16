package com.security.jwt.service;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.RoleCountDto;
import com.security.jwt.model.Role;
import com.security.jwt.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService {
    UserDetailsService userDetailsService();
    ApiResponse<List<User>> getAllUsers();  // Return ApiResponse<List<User>> instead of List<User>
    ApiResponse<List<User>> getUsersByRole(Role role);
    ApiResponse<User> updateUser(int id, User user);
    ApiResponse<User> getUserById(Integer id);
    ApiResponse<List<RoleCountDto>> getUserCountsForAllRoles();
    ApiResponse<String> sendOtpToUser(int userId);
    ApiResponse<String> verifyOtp(int userId, String otp);

}
