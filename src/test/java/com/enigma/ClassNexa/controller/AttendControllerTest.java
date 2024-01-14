package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.constan.ERole;
import com.enigma.ClassNexa.entity.*;
import com.enigma.ClassNexa.model.request.AttendDetailRequest;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.repository.*;
import com.enigma.ClassNexa.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private AttendService attendService;
    @Autowired
    private AttendRepository attendRepository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserCredentialRepository userCredentialRepository;
    @Autowired
    private RoleRepository roleRepository;
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
        Role rolep = new Role();
        rolep.setRole(ERole.ROLE_PARTICIPANT);
        rolep.setId("66");
        Role role = new Role();
        role.setRole(ERole.ROLE_TRAINER);
        role.setId("66");
        roleRepository.save(rolep);
        roleRepository.save(role);

        Attendance request = new Attendance();
        request.setId("6");
        request.setCategory("sad");
        attendanceRepository.save(request);

        List<Role> rolesp = new ArrayList<>();
        rolesp.add(rolep);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        UserCredential credential = new UserCredential();
        credential.setPassword("password");
        credential.setEmail("66@gmail.com");
        credential.setId("66");
        credential.setRoles(roles);

        UserCredential credential1 = new UserCredential();
        credential1.setPassword("password");
        credential1.setEmail("666@gmail.com");
        credential1.setId("66");
        credential1.setRoles(rolesp);
        userCredentialRepository.save(credential);
        userCredentialRepository.save(credential1);

        Trainer trainer = new Trainer();
        trainer.setAddress("test");
        trainer.setGender("test");
        trainer.setName("test");
        trainer.setPhoneNumber("test");
        trainer.setId("66");
        trainer.setUserCredential(credential);
        trainerRepository.save(trainer);

        Classes classes = new Classes();
        classes.setId("66");
        classes.setName("test");
        classes.setTrainer(trainer);
        classesRepository.save(classes);

        Participant participant = new Participant();
        participant.setGender("test");
        participant.setAddress("Test");
        participant.setName("test");
        participant.setPhoneNumber("test");
        participant.setId("66");
        participant.setUserCredential(credential1);
        participantRepository.save(participant);

        Attendance attendance = new Attendance();
        attendance.setId("6");
        attendance.setCategory("sad");
        attendanceRepository.save(attendance);

        Schedule schedule = new Schedule();
        Date date = new Date();
        Date endDate = new Date();
        endDate.setTime(System.currentTimeMillis() + 3600 * 1000);
        date.setTime(System.currentTimeMillis());
        schedule.setClasses_id(classes);
        schedule.setId("66");
        schedule.setMeeting_link("test");
        schedule.setStart_class(date);
        schedule.setEnd_class(endDate);
        scheduleRepository.save(schedule);

        List<AttendDetailRequest> detailRequests = new ArrayList<>();
        AttendDetailRequest attendDetailRequest = AttendDetailRequest.builder()
                .categoryId("6")
                .participantId(participant.getId())
                .build();
        detailRequests.add(attendDetailRequest);
        AttendRequest attendRequest = AttendRequest.builder()
                .scheduleId(schedule.getId())
                .attendDetailRequests(detailRequests)
                .build();
        Attend attend = Attend.builder()
                .id("66")
                .participant(participant)
                .attendance(request)
                .schedule(schedule)
                .absentReasons("")
                .build();
        attendRepository.save(attend);

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
                get("/api/attend?participantId=66")
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



        Role rolep = new Role();
        rolep.setRole(ERole.ROLE_PARTICIPANT);
        rolep.setId("66");
        Role role = new Role();
        role.setRole(ERole.ROLE_TRAINER);
        role.setId("66");
        roleRepository.save(rolep);
        roleRepository.save(role);

        Attendance request = new Attendance();
        request.setId("6");
        request.setCategory("sad");
        attendanceRepository.save(request);

        List<Role> rolesp = new ArrayList<>();
        rolesp.add(rolep);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        UserCredential credential = new UserCredential();
        credential.setPassword("password");
        credential.setEmail("66@gmail.com");
        credential.setId("66");
        credential.setRoles(roles);

        UserCredential credential1 = new UserCredential();
        credential1.setPassword("password");
        credential1.setEmail("666@gmail.com");
        credential1.setId("66");
        credential1.setRoles(rolesp);
        userCredentialRepository.save(credential);
        userCredentialRepository.save(credential1);

        Trainer trainer = new Trainer();
        trainer.setAddress("test");
        trainer.setGender("test");
        trainer.setName("test");
        trainer.setPhoneNumber("test");
        trainer.setId("66");
        trainer.setUserCredential(credential);
        trainerRepository.save(trainer);

        Classes classes = new Classes();
        classes.setId("66");
        classes.setName("test");
        classes.setTrainer(trainer);
        classesRepository.save(classes);

        Participant participant = new Participant();
        participant.setGender("test");
        participant.setAddress("Test");
        participant.setName("test");
        participant.setPhoneNumber("test");
        participant.setId("66");
        participant.setUserCredential(credential1);
        participantRepository.save(participant);

        Attendance attendance = new Attendance();
        attendance.setId("6");
        attendance.setCategory("sad");
        attendanceRepository.save(attendance);

        Schedule schedule = new Schedule();
        Date date = new Date();
        Date endDate = new Date();
        endDate.setTime(System.currentTimeMillis() + 3600 * 1000);
        date.setTime(System.currentTimeMillis());
        schedule.setClasses_id(classes);
        schedule.setId("66");
        schedule.setMeeting_link("test");
        schedule.setStart_class(date);
        schedule.setEnd_class(endDate);
        scheduleRepository.save(schedule);

        Attend attend = new Attend();
        attend.setAttendance(attendance);
        attend.setParticipant(participant);
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
