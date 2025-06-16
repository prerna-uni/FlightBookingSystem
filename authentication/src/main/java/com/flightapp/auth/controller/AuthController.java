package com.flightapp.auth.controller;

import com.flightapp.auth.model.User;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import com.flightapp.auth.repository.UserRepository;
import com.flightapp.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login for both admin & user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .toList();

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token
        ));
    }

    // Register users only (admin cannot register here)
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if ("admin".equalsIgnoreCase(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cannot register admin user"));
        }

        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhone(registerRequest.getPhone());

        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole("USER"); // default role USER

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    public static class AuthRequest {
        private String username;
        private String password;
        // getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
    	@NotBlank(message = "Username is required")
        private String username;
    	@NotBlank(message = "Password is required")
        private String password;
    	@NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
    	   @NotBlank(message = "Phone number is required")
    	    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
        private String phone;
        // getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone;}
    }}

