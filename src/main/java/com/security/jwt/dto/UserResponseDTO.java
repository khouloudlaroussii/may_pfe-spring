package com.security.jwt.dto;

import com.security.jwt.model.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Integer status;
    private Role role;
    private LocalDate birthday;
    private String phone;
    private String imagePath;
}
