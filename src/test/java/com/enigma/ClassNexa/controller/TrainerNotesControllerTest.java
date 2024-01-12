package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.TrainerNotes;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.ScheduleRequest;
import com.enigma.ClassNexa.model.request.TrainerNotesRequest;
import com.enigma.ClassNexa.model.response.ScheduleResponse;
import com.enigma.ClassNexa.model.response.TrainerNotesResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.repository.ClassesRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.repository.TrainerNotesRepository;
import com.enigma.ClassNexa.service.AuthService;
import com.enigma.ClassNexa.service.ClassesService;
import com.enigma.ClassNexa.service.ScheduleService;
import com.enigma.ClassNexa.service.TrainerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrainerNotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerNotesRepository trainerNotesRepository;

    @Autowired
    private ClassesService classesService;

    @Test
    void createTrainerNotesSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .build();
        String login = authService.login(loginRequest);

        Schedule byId = scheduleService.getByIdSchedule("82c17d6e-32fe-4b27-9156-2b33f00580dd");
        Trainer byTrainerId = trainerService.getByTrainerId("f8d649ff-d798-4262-9a65-4b65a4fcde2c");

        TrainerNotes trainerNotes = TrainerNotes.builder()
                .notes("disini lagi-lagi notes")
//                .trainer(byTrainerId)
                .schedule(byId)
                .build();

        mockMvc.perform(
                post("/api/notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerNotes))
                        .header("Authorization", login)

        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<TrainerNotes> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(trainerNotes.getId(), response.getData().getId());
            assertEquals(trainerNotes.getNotes(), response.getData().getNotes());
            assertEquals(trainerNotes.getTrainer(), response.getData().getTrainer());
            assertEquals(trainerNotes.getSchedule(), response.getData().getSchedule());

            assertTrue(trainerNotesRepository.existsById(response.getData().getId()));
            trainerNotesRepository.deleteById(response.getData().getId());
        });

    }

    @Test
    void getTrainerByIdSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .build();
        authService.login(loginRequest);
        Optional<TrainerNotes> notes = trainerNotesRepository.findById("744f62b3-5a9e-4c48-96c9-545cc57eafe6");

        TrainerNotesRequest trainerNotesRequest = TrainerNotesRequest.builder()
                .id(notes.get().getId())
                .notes(notes.get().getNotes())
                .trainer(notes.get().getTrainer().getId())
                .schedule(notes.get().getSchedule().getId())
                .build();

        mockMvc.perform(
                get("/api/notes/"+trainerNotesRequest.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerNotesRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TrainerNotesResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(trainerNotesRequest.getId(), response.getData().getId());
            assertEquals(trainerNotesRequest.getNotes(), response.getData().getNotes());
            assertEquals(trainerNotesRequest.getTrainer(), response.getData().getTrainer_id());
            assertEquals(trainerNotesRequest.getSchedule(), response.getData().getSchedule_id());

        });
    }

    @Test
    void updateTrainerNotesSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .build();
        authService.login(loginRequest);

        Optional<TrainerNotes> schedule = trainerNotesRepository.findById("8bd0afae-2fc0-4e83-ac97-84e4800a4195");

        TrainerNotesRequest trainerNotesRequest = TrainerNotesRequest.builder()
                .id(schedule.get().getId())
                .notes("notes oiiii")
                .trainer("f8d649ff-d798-4262-9a65-4b65a4fcde2c")
                .schedule("273ac798-7e78-4375-be8b-0953e5612a8c")
                .build();

        mockMvc.perform(
                put("/api/notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerNotesRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<TrainerNotesResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(trainerNotesRequest.getId(), response.getData().getId());
            assertEquals(trainerNotesRequest.getNotes(), response.getData().getNotes());
            assertEquals(trainerNotesRequest.getTrainer(), response.getData().getTrainer_id());
            assertEquals(trainerNotesRequest.getSchedule(), response.getData().getSchedule_id());

            assertTrue(trainerNotesRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getAllTrainerNotesSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .build();
        authService.login(loginRequest);

        mockMvc.perform(
                get("/api/notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ScheduleResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(12, response.getData().size());
        });
    }

    @Test
    void deleteTrainerNotesSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trainer@gmail.com")
                .password("password")
                .build();
        authService.login(loginRequest);

        Optional<TrainerNotes> byId = trainerNotesRepository.findById("b3a02b7a-2d58-4ecf-95a2-c06ca20eac12");
        Trainer byTrainerId = trainerService.getByTrainerId(byId.get().getTrainer().getId());
        ScheduleResponse byId1 = scheduleService.getById(byId.get().getSchedule().getId());
        Classes id = classesService.getId(byId1.getClasses_id());

        Schedule schedule = Schedule.builder()
                .id(byId1.getId())
                .meeting_link(byId1.getMeeting_link())
                .start_class(byId1.getStart_class())
                .end_class(byId1.getEnd_class())
                .classes_id(id)
                .build();

        TrainerNotes trainerNotes = TrainerNotes.builder()
                .notes("jago notess")
                .trainer(byTrainerId)
                .schedule(schedule)
                .build();
        TrainerNotes save = trainerNotesRepository.save(trainerNotes);

        mockMvc.perform(
                delete("/api/notes/"+save.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals("ok", response.getData());

        });
    }
}