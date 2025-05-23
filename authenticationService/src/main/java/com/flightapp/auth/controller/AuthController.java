package com.flightapp.auth.controller;

import com.flightapp.auth.dto.AuthResponse;
import com.flightapp.auth.dto.LoginRequest;
import com.flightapp.auth.dto.RegisterRequest;
import com.flightapp.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authenticationService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}