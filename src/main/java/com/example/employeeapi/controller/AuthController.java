package com.example.employeeapi.controller;

import com.example.employeeapi.model.User;
import com.example.employeeapi.repository.UserRepository;
import com.example.employeeapi.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    // DTO CLASSES
   
    public static class RegisterRequest {
        @NotBlank
        public String username;
        @NotBlank
        public String password;
        public String role; // optional, defaults to USER
    }

    public static class LoginRequest {
        @NotBlank
        public String username;
        @NotBlank
        public String password;
    }

    public static class TokenResponse {
        public String token;
        public TokenResponse(String token) { this.token = token; }
    }


    // REGISTER
  
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.findByUsername(req.username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }
        User u = new User();
        u.setUsername(req.username);
        u.setPassword(passwordEncoder.encode(req.password));
        u.setRole((req.role == null || req.role.isBlank()) ? "USER" : req.role.toUpperCase()); // default USER
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

   
    // LOGIN
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        var userOpt = userRepository.findByUsername(req.username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        var u = userOpt.get();
        if (!passwordEncoder.matches(req.password, u.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtUtil.generateToken(u.getUsername(), u.getRole());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
