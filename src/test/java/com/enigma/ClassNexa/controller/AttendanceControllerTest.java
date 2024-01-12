package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.repository.AttendanceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @BeforeEach
//    void setUp() {
//        attendanceRepository.deleteAll();
//    }


    @Test
    void createAttendanceSuccess() throws Exception {
        Attendance request = new Attendance();
        request.setId("6");
        request.setCategory("sad");
        mockMvc.perform(post("/api/attendance")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isCreated());
    }
    @Test
    void getAttendanceNotFound() throws Exception {
        mockMvc.perform(
                get("/api/attendance/7")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound());
    }
    @Test
    void getAttendanceSuccess() throws Exception {
        mockMvc.perform(
                get("/api/attendance/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk());
    }



    @Test
    void createAttendanceBadRequest() throws Exception {
        Attendance request = new Attendance();
        request.setId("8");

        mockMvc.perform(
                post("/api/attendance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()

        );
    }
}
