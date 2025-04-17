package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Focused (slice) test for {@link AuthController}.
 *
 * – @WebMvcTest starts only web‑layer beans (Controller, ControllerAdvice,
 *   MessageConverters, etc.)
 * – @AutoConfigureMockMvc(addFilters = false) disables the Spring‑Security
 *   filter‑chain so we don’t need a real JWT for these endpoints.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;          // injected automatically by WebMvcTest

    @MockBean                                // Mocked & injected into controller
    private AuthService authService;

    @Test
    @DisplayName("POST /api/register returns AuthResponse")
    void registerReturnsAuthResponse() throws Exception {

        RegisterRequest req = new RegisterRequest(
                "alice",                    // username
                "password",                 // password
                "alice@example.com",        // email
                "USER"                      // role
        );
        AuthResponse resp =
                new AuthResponse("alice", "alice@example.com", "USER", "jwt‑123");

        BDDMockito.given(authService.register(req)).willReturn(resp);

        mockMvc.perform(
                        post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("alice")))
                .andExpect(jsonPath("$.email", is("alice@example.com")))
                .andExpect(jsonPath("$.role", is("USER")))
                .andExpect(jsonPath("$.token", is("jwt‑123")));
    }

    @Test
    @DisplayName("POST /api/login returns AuthResponse")
    void loginReturnsAuthResponse() throws Exception {

        LoginRequest req = new LoginRequest("bob", "secret");
        AuthResponse resp =
                new AuthResponse("bob", "bob@example.com", "ADMIN", "jwt‑999");

        BDDMockito.given(authService.login(req)).willReturn(resp);

        mockMvc.perform(
                        post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("bob")))
                .andExpect(jsonPath("$.email", is("bob@example.com")))
                .andExpect(jsonPath("$.role", is("ADMIN")))
                .andExpect(jsonPath("$.token", is("jwt‑999")));
    }
}