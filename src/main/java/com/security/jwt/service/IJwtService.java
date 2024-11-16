package com.security.jwt.service;

import com.security.jwt.model.Role;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface IJwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails, Integer userId, String email, Role role,String firstName,String lastName ,String imagePath,boolean connection_otp);
    // extraction claims
    <T> T extractClaims(String token, Function<Claims, T> claimsResolver);

    // checking expiration
    Date getExpirationDateFromToken(String token);

    Boolean validateToken(String token, UserDetails userDetails);

}
