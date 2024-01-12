package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.ScheduleRequest;
import com.enigma.ClassNexa.model.response.ScheduleResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.repository.ClassesRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ScheduleControllerTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("admin@gmail.com")
    private String email;

    @Value("password")
    private String password;

    @Autowired
    private AuthService authService;

    @Test
    void createScheduleSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDateStart = dateFormat.parse("2023-10-31 15:30:00");
        Date currentDateEnd = dateFormat.parse("2023-10-31 16:30:00");
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .meeting_link("zoomyooo")
                .start_class(currentDateStart)
                .end_class(currentDateEnd)
                .classes_id("1")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/schedule")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest))
                        .header("Authorization", token)
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<ScheduleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(scheduleRequest.getMeeting_link(), response.getData().getMeeting_link());
            assertEquals(scheduleRequest.getStart_class(), response.getData().getStart_class());
            assertEquals(scheduleRequest.getEnd_class(), response.getData().getEnd_class());
            assertEquals(scheduleRequest.getClasses_id(), response.getData().getClasses_id());

            assertTrue(scheduleRepository.existsById(response.getData().getId()));
            scheduleRepository.deleteById(response.getData().getId());
        });

    }

    @Test
    void getScheduleByIdSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);
        Schedule schedule = scheduleRepository.findById("a7f0be4b-c8d5-4fba-be7b-dfd623a0895a").orElseThrow();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDateStart = dateFormat.parse(String.valueOf(schedule.getStart_class()));
        Date currentDateEnd = dateFormat.parse(String.valueOf(schedule.getEnd_class()));
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .id(schedule.getId())
                .meeting_link(schedule.getMeeting_link())
                .start_class(currentDateStart)
                .end_class(currentDateEnd)
                .classes_id(schedule.getClasses_id().getId())
                .build();

        mockMvc.perform(
                get("/api/schedule/"+schedule.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ScheduleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(schedule.getId(), response.getData().getId());
            assertEquals(scheduleRequest.getMeeting_link(), response.getData().getMeeting_link());
            assertEquals(scheduleRequest.getStart_class(), response.getData().getStart_class());
            assertEquals(scheduleRequest.getEnd_class(), response.getData().getEnd_class());
            assertEquals(scheduleRequest.getClasses_id(), response.getData().getClasses_id());
        });
    }

    @Test
    void updateScheduleSuccess() throws Exception {
        Schedule schedule = scheduleRepository.findById("713c432a-0d9d-4093-b76c-f7eeaf0e01e7").orElseThrow();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDateStart = dateFormat.parse(String.valueOf(schedule.getStart_class()));
        Date currentDateEnd = dateFormat.parse(String.valueOf(schedule.getEnd_class()));
        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .id(schedule.getId())
                .meeting_link("disini meeting link baru")
                .start_class(currentDateStart)
                .end_class(currentDateEnd)
                .classes_id("1")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/schedule")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleRequest))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<ScheduleResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(scheduleRequest.getId(), response.getData().getId());
            assertEquals(scheduleRequest.getMeeting_link(), response.getData().getMeeting_link());
            assertEquals(scheduleRequest.getStart_class(), response.getData().getStart_class());
            assertEquals(scheduleRequest.getEnd_class(), response.getData().getEnd_class());
            assertEquals(scheduleRequest.getClasses_id(), response.getData().getClasses_id());

            assertTrue(scheduleRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getAllScheduleSuccess() throws Exception {

        mockMvc.perform(
                get("/api/schedule")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ScheduleResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(13, response.getData().size());
        });
    }

    @Test
    void deleteScheduleSuccess() throws Exception {

        Optional<Classes> byId = classesRepository.findById("1");
        Classes classes = Classes.builder()
                .id(byId.get().getId())
                .name(byId.get().getName())
                .trainer(byId.get().getTrainer())
                .build();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDateStart = dateFormat.parse("2024-01-11 03:54:42");
        Date currentDateEnd = dateFormat.parse("2024-01-11 03:54:42");
        log.info(String.valueOf(currentDateEnd));
        Schedule scheduleRequest = Schedule.builder()
                .meeting_link("disini meeting link baru")
                .start_class(currentDateStart)
                .end_class(currentDateEnd)
                .classes_id(classes)
                .build();
        Schedule save = scheduleRepository.save(scheduleRequest);

        mockMvc.perform(
                delete("/api/schedule/"+save.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals("ok", response.getData());

            assertFalse(scheduleRepository.existsById("test"));
        });
    }

}