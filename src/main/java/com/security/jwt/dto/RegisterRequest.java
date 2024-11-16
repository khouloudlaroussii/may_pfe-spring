package com.security.jwt.dto;

import com.security.jwt.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Birthday is required")
    private LocalDate birthday;

    @NotNull(message = "address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "Role is required")
    private Role role;  // Role should be provided by the client

    private String imagePath;
}
