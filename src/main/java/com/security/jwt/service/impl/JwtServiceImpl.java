package com.security.jwt.service.impl;

import com.security.jwt.model.Role;
import com.security.jwt.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {

    private final Key SECRET_KEY = getSigning();

    // 21 days in milliseconds
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 21;

    // generating token with 21-day expiration and including userId and email
    public String generateToken(UserDetails userDetails, Integer userId, String email, Role role,String firstName ,String lastName,String imagePath,boolean connection_otp) {
        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
                .claim("userId", userId)       // Adding user ID claim
                .claim("email", email)         // Adding email claim
                .claim("role", role)         // Adding role claim
                .claim("firstName", firstName)         // Adding role claim
                .claim("lastName", lastName)         // Adding role claim
                .claim("imagePath", imagePath)         // Adding role claim
                .claim("connection_otp", connection_otp)         // Adding connection otp claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Set expiration to 21 days
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // extracting userName
    @Override
    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // extracting user ID
    public Integer extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Integer.class));
    }

    // extracting email
    public String extractEmail(String token) {
        return extractClaims(token, claims -> claims.get("email", String.class));
    }

    // extraction claims
    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // signing key
    private Key getSigning() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // checking expiration
    @Override
    public Date getExpirationDateFromToken(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // validation token
    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
