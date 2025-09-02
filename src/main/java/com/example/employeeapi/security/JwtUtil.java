package com.example.employeeapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    //  Default values if not provided in application.properties
    
    private static final String DEFAULT_SECRET = "my_super_secret_key_that_is_at_least_32_chars_long_123";
    private static final long DEFAULT_EXPIRATION = 3600000; // 1 hour

    public JwtUtil(
            @Value("${app.jwt.secret:" + DEFAULT_SECRET + "}") String secret,
            @Value("${app.jwt.expiration-ms:" + DEFAULT_EXPIRATION + "}") long expirationMs
    ) {
    	if (secret.length() < 32) {
    	    throw new IllegalArgumentException("JWT secret must be at least 32 characters long!");
    	}

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    //  Generate token with username + role
    
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    
    
    
    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }

   
    
    
    public String extractRole(String token) {
        Object r = parse(token).getBody().get("role");
        return r == null ? null : r.toString();
    }

    
    
    // 📌 Validate token
    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    

    // 📌 Parse and verify JWT
    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
