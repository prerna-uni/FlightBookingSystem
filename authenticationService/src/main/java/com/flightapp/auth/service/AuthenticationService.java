package com.flightapp.auth.service;

import com.flightapp.auth.dto.LoginRequest;
import com.flightapp.auth.dto.RegisterRequest;
import com.flightapp.auth.model.Role;
import com.flightapp.auth.model.User;
import com.flightapp.auth.repository.UserRepository;
import com.flightapp.auth.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // default role
        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        // Authenticate using Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return JWTService.generateToken(user);
    }}