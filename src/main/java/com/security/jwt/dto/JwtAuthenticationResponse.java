package com.security.jwt.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    private Integer id;
    private String imagePath;
    private Boolean connectionOtp;
    private Integer status;
}
