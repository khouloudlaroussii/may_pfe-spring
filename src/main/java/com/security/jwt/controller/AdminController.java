package com.security.jwt.controller;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.RoleCountDto;
import com.security.jwt.model.Role;
import com.security.jwt.model.User;
import com.security.jwt.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final IUserService userService;

    @GetMapping()
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("HI ADMIN");
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ApiResponse<List<User>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable("role") Role role) {
        ApiResponse<List<User>> response = userService.getUsersByRole(role);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable("id") int id, @RequestBody User userUpdates) {
        ApiResponse<User> response = userService.updateUser(id, userUpdates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") Integer id) {
        ApiResponse<User> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/count-by-role")
    public ResponseEntity<ApiResponse<List<RoleCountDto>>> getUserCountsForAllRoles() {
        ApiResponse<List<RoleCountDto>> response = userService.getUserCountsForAllRoles();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtpToUser(@PathVariable Integer id) {
        ApiResponse<String> response = userService.sendOtpToUser(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(
            @RequestParam("userId") int userId,
            @RequestParam("otp") String otp) {
        ApiResponse<String> response = userService.verifyOtp(userId, otp);
        return ResponseEntity.ok(response);
    }
}
