package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Trainer;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private DetailClassParticipantRepository detailClassParticipantRepository;

    @Value("${app.class-nexa.email}")
    private String email;

    @Value("${app.class-nexa.password}")
    private String password;


    @Test
    void loginAdminSuccess() throws Exception {
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
    void createdSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        Participant participant = new Participant();
        participant.setName("Anka");
        participant.setAddress("Pesona Jakarta Street");
        participant.setGender("Male");
        participant.setPhoneNumber("081398817317");
        participantRepository.save(participant);

        DetailClassParticipantRequest classParticipantRequest = new DetailClassParticipantRequest();
        classParticipantRequest.setId(participant.getId());

        List<DetailClassParticipantRequest> participantRequestList = new ArrayList<>();
        participantRequestList.add(classParticipantRequest);

        Trainer trainer = new Trainer();
        trainer.setName("Rindu");
        trainer.setAddress("Jagakarsa III");
        trainer.setPhoneNumber("081398817317");
        trainer.setGender("Female");
        trainerRepository.save(trainer);

        ClassesRequest request = new ClassesRequest();
        request.setName("java batch 1");
        request.setTrainerId(trainer.getId());
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
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        Participant participant = new Participant();
        participant.setName("Dirga");
        participant.setAddress("Pesona Jakarta Street");
        participant.setGender("Male");
        participant.setPhoneNumber("081398817317");
        participantRepository.save(participant);

        DetailClassParticipantRequest classParticipantRequest = new DetailClassParticipantRequest();
        classParticipantRequest.setId(participant.getId());

        List<DetailClassParticipantRequest> participantRequestList = new ArrayList<>();
        participantRequestList.add(classParticipantRequest);

        Trainer trainer = new Trainer();
        trainer.setName("Cecil");
        trainer.setAddress("Jagakarsa III");
        trainer.setPhoneNumber("081398817317");
        trainer.setGender("Female");
        trainerRepository.save(trainer);

        Classes classes = new Classes();
        classes.setName("java batch 2");
        classes.setTrainer(trainer);
        classesRepository.save(classes);

        UpdateClassesRequest request = new UpdateClassesRequest();
        request.setId(classes.getId());
        request.setName(classes.getName());
        request.setTrainerId(classes.getTrainer().getId());
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
                .email(email)
                .password(password)
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
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        Trainer trainer = new Trainer();
        trainer.setName("Laura");
        trainer.setAddress("Jagakarsa III");
        trainer.setPhoneNumber("081398817317");
        trainer.setGender("Female");
        trainerRepository.save(trainer);

        Classes classes = new Classes();
        classes.setName("java batch 3");
        classes.setTrainer(trainer);
        classesRepository.save(classes);

        Participant participant = new Participant();
        participant.setName("Raja");
        participant.setAddress("Pesona Jakarta Street");
        participant.setGender("Male");
        participant.setPhoneNumber("081398817317");
        participantRepository.save(participant);

        DetailClassParticipant detailClassParticipant = new DetailClassParticipant();
        detailClassParticipant.setParticipant(participant);
        detailClassParticipant.setClasses(classes);
        detailClassParticipantRepository.save(detailClassParticipant);


        String id = classes.getId();


        {
            mockMvc.perform(
                    get("/api/classes/{id}",id)
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
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        Trainer trainer = new Trainer();
        trainer.setName("Mia");
        trainer.setAddress("Jagakarsa III");
        trainer.setPhoneNumber("081398817317");
        trainer.setGender("Female");
        trainerRepository.save(trainer);

        Classes classes = new Classes();
        classes.setName("java batch 4");
        classes.setTrainer(trainer);
        classesRepository.save(classes);

        {
            mockMvc.perform(
                    delete("/api/classes/{id}", classes.getId())
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


        List<Classes> classes = classesRepository.findAll();

        {
            mockMvc.perform(
                    get("/api/classes")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(classes))
            ).andExpectAll(
                    status().isForbidden()
            );
        }
    }

    @Test
    void updatedForDeleteParticipantSuccess() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        String token = authService.login(loginRequest);

        Participant participant = new Participant();
        participant.setName("Alex");
        participant.setAddress("Pesona Jakarta Street");
        participant.setPhoneNumber("081398817317");
        participantRepository.save(participant);

        DetailClassParticipantRequest classParticipantRequest = new DetailClassParticipantRequest();
        classParticipantRequest.setId(participant.getId());

        List<DetailClassParticipantRequest> participantRequestList = new ArrayList<>();
        participantRequestList.add(classParticipantRequest);

        Trainer trainer = new Trainer();
        trainer.setName("Mawar");
        trainer.setAddress("Jagakarsa III");
        trainer.setPhoneNumber("081398817317");
        trainer.setGender("Female");
        trainerRepository.save(trainer);

        Classes classes = new Classes();
        classes.setName("java batch 5");
        classes.setTrainer(trainer);
        classesRepository.save(classes);

        UpdateClassesRequest request = new UpdateClassesRequest();
        request.setId(classes.getId());
        request.setName(classes.getName());
        request.setTrainerId(classes.getTrainer().getId());
        request.setParticipants(participantRequestList);
        classesRepository.findById(request.getId());

        {
            mockMvc.perform(
                    put("/api/classes/details/participants")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .header("Authorization", token)
            ).andExpectAll(
                    status().isOk()
            );
        }
    }

}
