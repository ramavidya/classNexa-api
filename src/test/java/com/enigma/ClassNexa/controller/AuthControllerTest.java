package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.RegisterRequest;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.repository.UserCredentialRepository;
import com.enigma.ClassNexa.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired UserCredentialRepository userCredentialRepository;

    @Value("${app.class-nexa.email}")
    private String email;

    @Value("${app.class-nexa.password}")
    private String password;

    @Test
    @Order(1)
    void registerAdminSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        RegisterRequest buildAdmin = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .name("admin")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        UserCredential userCredential = userCredentialRepository.findByEmail(buildAdmin.getEmail()).orElse(null);
        if (userCredential == null) {
            mockMvc.perform(
                    post("/api/auth/register/admin")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildAdmin))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isCreated()
            );
        }
    }
    @Test
    @Order(2)
    void registerTrainerSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        RegisterRequest buildTrainer = RegisterRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .name("trainer")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        UserCredential userCredential = userCredentialRepository.findByEmail(buildTrainer.getEmail()).orElse(null);
        if (userCredential == null) {
            mockMvc.perform(
                    post("/api/auth/register/trainer")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildTrainer))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isCreated()
            );
        }
    }
    @Test
    @Order(3)
    void registerParticipanSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        RegisterRequest buildParticipant = RegisterRequest.builder()
                .email("participant@gmail.com")
                .password("password")
                .name("participant")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        UserCredential userCredential = userCredentialRepository.findByEmail(buildParticipant.getEmail()).orElse(null);
        if (userCredential == null) {
            mockMvc.perform(
                    post("/api/auth/register/participant")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildParticipant))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isCreated()
            );
        }
    }
    @Test
    @Order(4)
    void loginSuperAdminSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getData());
                }
        );
    }
    @Test
    @Order(5)
    void loginAdminSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .build();
        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getData());
                }
        );
    }
    @Test
    @Order(6)
    void loginTrainerSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .build();
        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getData());
                }
        );
    }
    @Test
    @Order(7)
    void loginParticipantSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("participant@gmail.com")
                .password("password")
                .build();
        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getData());
                }
        );
    }
    @Test
    @Order(8)
    void loginFailed() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("error@gmail.com")
                .password("error")
                .build();
        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    @Order(9)
    void registerAdminFailedForbidden() throws Exception {
        RegisterRequest buildAdmin = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .name("admin")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        UserCredential userCredential = userCredentialRepository.findByEmail(buildAdmin.getEmail()).orElse(null);
        if (userCredential == null) {
            mockMvc.perform(
                    post("/api/auth/register/admin")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildAdmin))
            ).andExpectAll(
                    status().isForbidden()
            );
        }
    }
    @Test
    @Order(10)
    void registerTrainerFailedForbidden() throws Exception {
        RegisterRequest buildTrainer = RegisterRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .name("trainer")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        UserCredential userCredential = userCredentialRepository.findByEmail(buildTrainer.getEmail()).orElse(null);
        if (userCredential == null) {
            mockMvc.perform(
                    post("/api/auth/register/trainer")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildTrainer))
            ).andExpectAll(
                    status().isForbidden()
            );
        }
    }
    @Test
    @Order(11)
    void registerParticipantFailedForbidden() throws Exception {
        RegisterRequest buildParticipant = RegisterRequest.builder()
                .email("participant@gmail.com")
                .password("password")
                .name("participant")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        UserCredential userCredential = userCredentialRepository.findByEmail(buildParticipant.getEmail()).orElse(null);
        if (userCredential == null) {
            mockMvc.perform(
                    post("/api/auth/register/participant")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildParticipant))
            ).andExpectAll(
                    status().isForbidden()
            );
        }
    }
    @Test
    @Order(12)
    void registerAdminConflig() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        RegisterRequest buildAdmin = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .name("admin")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

            mockMvc.perform(
                    post("/api/auth/register/admin")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildAdmin))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isConflict()
            );
    }
    @Test
    @Order(13)
    void registerTrainerConflig() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        RegisterRequest buildTrainer = RegisterRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .name("trainer")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        mockMvc.perform(
                post("/api/auth/register/trainer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildTrainer))
                        .header("Authorization", token)
        ).andExpectAll(
                status().isConflict()
        );
    }

    @Test
    @Order(14)
    void registerParticipantConflig() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        RegisterRequest buildParticipant = RegisterRequest.builder()
                .email("participant@gmail.com")
                .password("password")
                .name("participant")
                .gender("male")
                .address("jl.sesama")
                .phoneNumber("085895780479")
                .build();

        mockMvc.perform(
                post("/api/auth/register/participant")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildParticipant))
                        .header("Authorization", token)
        ).andExpectAll(
                status().isConflict()
        );
    }
}