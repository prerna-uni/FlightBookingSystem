package com.flightapp.auth.controller;

import com.flightapp.auth.model.User;
import com.flightapp.auth.repository.UserRepository;
import com.flightapp.auth.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthController.AuthRequest loginRequest = new AuthController.AuthRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        // Correct Mockito for authenticate (returns Authentication, not void)
        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User("testuser", "encodedPassword", authorities);

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        when(jwtUtil.generateToken("testuser", List.of("ROLE_USER"))).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername("testuser");
        verify(jwtUtil).generateToken("testuser", List.of("ROLE_USER"));
    }

    @Test
    void testLoginBadCredentials() throws Exception {
        AuthController.AuthRequest loginRequest = new AuthController.AuthRequest();
        loginRequest.setUsername("wronguser");
        loginRequest.setPassword("wrongpass");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));

        verify(authenticationManager).authenticate(any());
        verifyNoMoreInteractions(userDetailsService, jwtUtil);
    }

    @Test
    void testRegisterSuccess() throws Exception {
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("newpass");

        when(userRepository.findByUsername("newuser")).thenReturn(null);
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        verify(userRepository).findByUsername("newuser");
        verify(passwordEncoder).encode("newpass");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterAdminUsernameRejected() throws Exception {
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest();
        registerRequest.setUsername("admin");
        registerRequest.setPassword("somepass");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Cannot register admin user"));

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUsernameExists() throws Exception {
        AuthController.RegisterRequest registerRequest = new AuthController.RegisterRequest();
        registerRequest.setUsername("existinguser");
        registerRequest.setPassword("pass");

        when(userRepository.findByUsername("existinguser")).thenReturn(new User());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"));

        verify(userRepository).findByUsername("existinguser");
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }
}
