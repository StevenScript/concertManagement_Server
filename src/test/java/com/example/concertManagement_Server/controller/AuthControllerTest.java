package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AuthController, verifying JSON request/response
 * for both registration and login endpoints.
 */
class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthService authService;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        authService = Mockito.mock(AuthService.class);
        AuthController controller = new AuthController(authService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /** Verifies POST /api/register returns a valid AuthResponse JSON. */
    @Test
    void registerReturnsAuthResponse() throws Exception {
        RegisterRequest req = new RegisterRequest("alice", "alice@example.com", "password", "USER");
        AuthResponse resp = new AuthResponse("alice", "alice@example.com", "USER", "jwt-123");
        given(authService.register(req)).willReturn(resp);

        mockMvc.perform(post("/api/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.token").value("jwt-123"));
    }

    /** Verifies POST /api/login returns a valid AuthResponse JSON. */
    @Test
    void loginReturnsAuthResponse() throws Exception {
        LoginRequest req = new LoginRequest("bob", "secret");
        AuthResponse resp = new AuthResponse("bob", "bob@example.com", "ADMIN", "jwt-999");
        given(authService.login(req)).willReturn(resp);

        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.token").value("jwt-999"));
    }
}