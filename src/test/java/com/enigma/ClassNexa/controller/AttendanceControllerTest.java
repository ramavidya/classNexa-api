package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.repository.AttendanceRepository;
import com.enigma.ClassNexa.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;

    @Value("${app.class-nexa.email}")
    private String email;

    @Value("${app.class-nexa.password}")
    private String password;

    @Test
    void createAttendanceSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        Attendance request = new Attendance();
        request.setId("6");
        request.setCategory("sad");
        attendanceRepository.save(request);
        mockMvc.perform(post("/api/attendance")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", token)
        ).andExpectAll(status().isCreated());
    }
    @Test
    void getAttendanceNotFound() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        mockMvc.perform(
                get("/api/attendance/7")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andExpectAll(
                status().isNotFound());
    }
    @Test
    void getAttendanceSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        mockMvc.perform(
                get("/api/attendance/6")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andExpectAll(
                status().isOk());
    }



    @Test
    void createAttendanceBadRequest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        Attendance request = new Attendance();
        request.setId("8");

        mockMvc.perform(
                post("/api/attendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token)

        ).andExpectAll(
                status().isBadRequest()

        );
    }
    @Test
    void createAttendanceForbidden() throws Exception {

        Attendance request = new Attendance();
        request.setId("8");

        mockMvc.perform(
                post("/api/attendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isForbidden()

        );
    }
    @Test
    void deleteAttendanceNotFound() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);



        mockMvc.perform(
                delete("/api/attendance/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)

        ).andExpectAll(
                status().isNotFound()

        );
    }
    @Test
    void deleteAttendanceSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        Attendance request = new Attendance();
        request.setId("9");
        request.setCategory("happy");
        attendanceRepository.save(request);

        mockMvc.perform(
                delete("/api/attendance/9")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)

        ).andExpectAll(
                status().isOk()

        );
    }
}
