package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Questions;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.QuestionsRequest;
import com.enigma.ClassNexa.model.request.UpdateStatusRequest;
import com.enigma.ClassNexa.model.response.QuestionsResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionsControllerTest {

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;




    @Test
    void createQuestionsSuccess() throws Exception {
        LoginRequest requestLogin = new LoginRequest();
        requestLogin.setEmail("participant1@gmail.com");
        requestLogin.setPassword("password");
        authService.login(requestLogin);

        Schedule schedule = scheduleService.getByIdSchedule("82c17d6e-32fe-4b27-9156-2b33f00580dd");

        QuestionsRequest request = new QuestionsRequest();
        request.setQuestion("testttt");
        request.setCourse("java fundamental");
        request.setChapter("chapter 15");
        request.setStatus(false);
        request.setScheduleId(schedule.getId());
        questionsService.create(request);

        mockMvc.perform(
                post("/api/questions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<QuestionsResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals(HttpStatus.CREATED.getReasonPhrase(), response.getStatus());
            assertEquals("successfully create new Questions", response.getMessage());
            assertNotNull(response.getData());

        });
    }

    @Test
    void createQuestionsForbiden() throws Exception {

        Schedule schedule = scheduleService.getByIdSchedule("82c17d6e-32fe-4b27-9156-2b33f00580dd");

        QuestionsRequest request = new QuestionsRequest();
        request.setQuestion("testttt");
        request.setCourse("java fundamental");
        request.setChapter("chapter 15");
        request.setStatus(false);
        request.setScheduleId(schedule.getId());

        mockMvc.perform(
                post("/api/questions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isForbidden()
        );
    }


    @Test
    void getAllSuccess() throws Exception {
        LoginRequest requestLogin = new LoginRequest();
        requestLogin.setEmail("trainer1@gmail.com");
        requestLogin.setPassword("password");
        authService.login(requestLogin);

        mockMvc.perform(
                get("/api/questions" )
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<QuestionsResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals("successfully get all questions", response.getMessage());
            assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatus());
            assertEquals(1,response.getPagingResponse().getPage());
            assertEquals(10,response.getPagingResponse().getSize());
            assertNotNull(response.getData());

        });
    }

    @Test
    void getAllForbiden() throws Exception {

        mockMvc.perform(
                get("/api/questions" )
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    void updateQuestionsSuccess() throws Exception {
        LoginRequest requestLogin = new LoginRequest();
        requestLogin.setEmail("trainer1@gmail.com");
        requestLogin.setPassword("password");
        authService.login(requestLogin);

        Questions questions = questionsService.getById("358df948-fa5f-4b3d-bbe4-ea914db5fe3f");
        Schedule schedule = scheduleService.getByIdSchedule("82c17d6e-32fe-4b27-9156-2b33f00580dd");

        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setQuestionsId(questions.getId());
        request.setScheduleId(schedule.getId());
        request.setQuestionsId(questions.getId());
        questionsService.update(request);

        mockMvc.perform(
                put("/api/questions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpect(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<QuestionsResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals("Succesfully update status Questions", response.getMessage());
            assertEquals(HttpStatus.CREATED.getReasonPhrase(), response.getStatus());
            assertNotNull(response.getData());

        });

    }

    @Test
    void deleteQuestionsByIdSuccess() throws Exception {
        LoginRequest requestLogin = new LoginRequest();
        requestLogin.setEmail("trainer1@gmail.com");
        requestLogin.setPassword("password");
        authService.login(requestLogin);

        Questions questions = questionsService.getById("358df948-fa5f-4b3d-bbe4-ea914db5fe3f");

        mockMvc.perform(
                delete("/api/questions/" + questions.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals("succesfully update by id", response.getMessage());
            assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatus());
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void deleteQuestionsByIdForbiden() throws Exception {

        mockMvc.perform(
                delete("/api/questions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }










}