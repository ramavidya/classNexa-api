package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.constan.ERole;
import com.enigma.ClassNexa.entity.*;
import com.enigma.ClassNexa.model.request.AttendDetailRequest;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.repository.AttendRepository;
import com.enigma.ClassNexa.repository.AttendanceRepository;
import com.enigma.ClassNexa.repository.ParticipantRepository;
import com.enigma.ClassNexa.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AttendControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private AttendRepository attendRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Value("${app.class-nexa.email}")
    private String email;

    @Value("${app.class-nexa.password}")
    private String password;

    @Test
    void createAttendSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        List<AttendDetailRequest> attendDetailRequests = new ArrayList<>();

        AttendDetailRequest attendDetailRequest = new AttendDetailRequest();
        attendDetailRequest.setCategoryId("1");
        attendDetailRequest.setParticipantId("6");
        attendDetailRequests.add(attendDetailRequest);

        AttendRequest attendRequest = new AttendRequest();
        attendRequest.setScheduleId("1");
        attendRequest.setAttendDetailRequests(attendDetailRequests);

        mockMvc.perform(post("/api/attend")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attendRequest))
                .header("Authorization", token)
        ).andExpectAll(status().isCreated());
    }
    @Test
    void getAttendNotFound() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        mockMvc.perform(
                get("/api/attend/7")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andExpectAll(
                status().isNotFound());
    }
    @Test
    void getAttend() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);
        mockMvc.perform(
                get("/api/attend?participantId=2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andExpectAll(
                status().isOk());
    }
    @Test
    void createAttendBadRequest() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);


        mockMvc.perform(
                post("/api/attend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)

        ).andExpectAll(
                status().isBadRequest()

        );
    }
    @Test
    void createAttendForbidden() throws Exception {

        List<AttendDetailRequest> attendDetailRequests = new ArrayList<>();

        AttendDetailRequest attendDetailRequest = new AttendDetailRequest();
        attendDetailRequest.setCategoryId("1");
        attendDetailRequest.setParticipantId("6");
        attendDetailRequests.add(attendDetailRequest);

        AttendRequest attendRequest = new AttendRequest();
        attendRequest.setScheduleId("1");
        attendRequest.setAttendDetailRequests(attendDetailRequests);

        mockMvc.perform(
                post("/api/attendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attendRequest))

        ).andExpectAll(
                status().isForbidden()
        );
    }
    @Test
    void deleteAttendNotFound() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);



        mockMvc.perform(
                delete("/api/attend/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)

        ).andExpectAll(
                status().isNotFound()

        );
    }
    @Test
    void deleteAttendSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);


        Optional<Attendance> optionalAttendance = attendanceRepository.findById("4");
        Optional<Participant> optionalParticipant = participantRepository.findById("3");

        Attend attend = new Attend();
        attend.setAttendance(optionalAttendance.get());
        attend.setParticipant(optionalParticipant.get());
        attend.setAbsentReasons("diare");
        attendRepository.save(attend);
        String id = attend.getId();


        mockMvc.perform(
                delete("/api/attend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)

        ).andExpectAll(
                status().isOk()
        );
    }
}
