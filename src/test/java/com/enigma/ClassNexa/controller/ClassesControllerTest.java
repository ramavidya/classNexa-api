package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.model.request.*;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.repository.ClassesRepository;
import com.enigma.ClassNexa.repository.DetailClassParticipantRepository;
import com.enigma.ClassNexa.repository.ParticipantRepository;
import com.enigma.ClassNexa.repository.TrainerRepository;
import com.enigma.ClassNexa.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClassesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private ClassesRepository classesRepository;



    @Test
    void loginAdminSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("admin-classes@gmail.com")
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
    void createdSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin-classes@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        DetailClassParticipantRequest classParticipantRequest = new DetailClassParticipantRequest();
        classParticipantRequest.setId("23df3a1c-1bc7-4c7e-b3a4-ef312d6bc802");

        List<DetailClassParticipantRequest> participantRequestList = new ArrayList<>();
        participantRequestList.add(classParticipantRequest);

        ClassesRequest request = new ClassesRequest();
        request.setName("java batch 21");
        request.setTrainerId("afe27d13-6a9a-411d-86f2-ba8f24224ebd");
        request.setParticipants(participantRequestList);

        {
            mockMvc.perform(
                    post("/api/classes")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isCreated()
            );
        }
    }

    @Test
    void updatedSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin-classes@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        DetailClassParticipantRequest classParticipantRequest = new DetailClassParticipantRequest();
        classParticipantRequest.setId("0b83f30a-25c1-48fb-83fa-7656237f4ded");

        List<DetailClassParticipantRequest> participantRequestList = new ArrayList<>();
        participantRequestList.add(classParticipantRequest);

        UpdateClassesRequest request = new UpdateClassesRequest();
        request.setId("0033c465-f787-4287-8423-fec514e50a3c");
        request.setName("java batch 21");
        request.setTrainerId("afe27d13-6a9a-411d-86f2-ba8f24224ebd");
        request.setParticipants(participantRequestList);
        classesRepository.findById(request.getId());

        {
            mockMvc.perform(
                    put("/api/classes")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isOk()
            );
        }
    }

    @Test
    void getAllSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin-classes@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        List<Classes> classes = classesRepository.findAll();


        {
            mockMvc.perform(
                    get("/api/classes")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(classes))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isOk()
            );
        }
    }

    @Test
    void getByIdSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin-classes@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        Optional<Classes> classes = classesRepository.findById("0033c465-f787-4287-8423-fec514e50a3c");


        {
            mockMvc.perform(
                    get("/api/classes/0033c465-f787-4287-8423-fec514e50a3c")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(classes))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isOk()
            );
        }
    }

    @Test
    void deleteSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin-classes@gmail.com")
                .password("password")
                .build();
        String token = authService.login(loginRequest);

        {
            mockMvc.perform(
                    delete("/api/classes/0033c465-f787-4287-8423-fec514e50a3c")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString("Ok"))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isOk()
            );
        }
    }

    @Test
    void classesUnSuccess() throws Exception {

        DetailClassParticipantRequest classParticipantRequest = new DetailClassParticipantRequest();
        classParticipantRequest.setId("23df3a1c-1bc7-4c7e-b3a4-ef312d6bc802");

        List<DetailClassParticipantRequest> participantRequestList = new ArrayList<>();
        participantRequestList.add(classParticipantRequest);

        ClassesRequest request = new ClassesRequest();
        request.setName("java batch 21");
        request.setTrainerId("afe27d13-6a9a-411d-86f2-ba8f24224ebd");
        request.setParticipants(participantRequestList);

        {
            mockMvc.perform(
                    post("/api/classes")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            ).andExpectAll(
                    status().isForbidden()
            );
        }
    }

}
