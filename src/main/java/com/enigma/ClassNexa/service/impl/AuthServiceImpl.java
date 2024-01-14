package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.constan.ERole;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Role;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.*;
import com.enigma.ClassNexa.model.response.RegisterResponse;
import com.enigma.ClassNexa.repository.UserCredentialRepository;
import com.enigma.ClassNexa.security.JwtUtil;
import com.enigma.ClassNexa.service.*;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final TrainerService trainerService;
    private final ParticipantService participantService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${app.class-nexa.email}")
    private String email;

    @Value("${app.class-nexa.password}")
    private String password;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void initSuperAdmin() {
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByEmail(email);
        if (optionalUserCredential.isPresent())return;
        Role roleSuperAdmin = roleService.getOrSave(ERole.ROLE_SUPER_ADMIN);
        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);
        Role roleTrainer = roleService.getOrSave(ERole.ROLE_TRAINER);
        Role roleParticipant = roleService.getOrSave(ERole.ROLE_PARTICIPANT);
        String hashPassword = passwordEncoder.encode(password);
        UserCredential userCredential = UserCredential.builder()
                .email(email)
                .password(hashPassword)
                .roles(List.of(roleParticipant,roleTrainer,roleAdmin,roleSuperAdmin))
                .build();
        userCredentialRepository.saveAndFlush(userCredential);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse registerAdmin(RegisterRequest request) {
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByEmail(request.getEmail());
        if (optionalUserCredential.isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"email existed");
        Role roleAdmin = roleService.getOrSave(ERole.ROLE_ADMIN);
        Role roleTrainer = roleService.getOrSave(ERole.ROLE_TRAINER);
        Role rolePartisipan = roleService.getOrSave(ERole.ROLE_PARTICIPANT);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserCredential buildUserCredential = UserCredential.builder()
                .email(request.getEmail())
                .password(hashPassword)
                .roles(List.of(rolePartisipan,roleTrainer,roleAdmin))
                .build();
        UserCredential userCredential = userCredentialRepository.saveAndFlush(buildUserCredential);
        List<String> listRole = userCredential.getRoles().stream().map(role -> role.getRole().name()).toList();
        String admin = adminService.create(UserCreateRequest.builder()
                                         .name(request.getName())
                                          .address(request.getAddress())
                                            .gender(request.getGender())
                                              .phoneNumber(request.getPhoneNumber())
                                                .userCredential(userCredential)
                                                   .build());
        return RegisterResponse.builder()
                .email(userCredential.getEmail())
                .name(admin)
                .roles(listRole)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse registerTrainer(RegisterRequest request) throws IOException {
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByEmail(request.getEmail());
        if (optionalUserCredential.isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"email existed");
        Role roleTrainer = roleService.getOrSave(ERole.ROLE_TRAINER);
        Role rolePartisipan = roleService.getOrSave(ERole.ROLE_PARTICIPANT);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserCredential buildUserCredential = UserCredential.builder()
                .email(request.getEmail())
                .password(hashPassword)
                .roles(List.of(rolePartisipan,roleTrainer))
                .build();
        UserCredential userCredential = userCredentialRepository.saveAndFlush(buildUserCredential);
        List<String> listRole = userCredential.getRoles().stream().map(role -> role.getRole().name()).toList();
        String trainer = trainerService.create(UserCreateRequest.builder()
                .name(request.getName())
                .address(request.getAddress())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(userCredential)
                .build());
        return RegisterResponse.builder()
                .email(userCredential.getEmail())
                .name(trainer)
                .roles(listRole)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse registerParticipant(RegisterRequest request) throws IOException {
        Optional<UserCredential> optionalUserCredential = userCredentialRepository.findByEmail(request.getEmail());
        if (optionalUserCredential.isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"email existed");
        Role rolePartisipan = roleService.getOrSave(ERole.ROLE_PARTICIPANT);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserCredential buildUserCredential = UserCredential.builder()
                .email(request.getEmail())
                .password(hashPassword)
                .roles(List.of(rolePartisipan))
                .build();
        UserCredential userCredential = userCredentialRepository.saveAndFlush(buildUserCredential);
        List<String> listRole = userCredential.getRoles().stream().map(role -> role.getRole().name()).toList();
        String participan = participantService.create(UserCreateRequest.builder()
                .name(request.getName())
                .address(request.getAddress())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(userCredential)
                .build());
        return RegisterResponse.builder()
                .email(userCredential.getEmail())
                .name(participan)
                .roles(listRole)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(LoginRequest request) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserCredential userCredential = (UserCredential) authentication.getPrincipal();
        return jwtUtil.generateToken(userCredential);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer uploadParticipant(MultipartFile file) throws IOException {
        Set<UploadRequest> uploadRequests = parseCsv(file);

        List<UserCredential> userCredentialList = new ArrayList<>();
        List<Participant> particpantList = new ArrayList<>();

        for (UploadRequest request : uploadRequests) {
            UserCredential optional = userCredentialRepository.findByEmail(request.getEmail()).orElse(null);
            if (optional == null) {
                UserCredential builUserCredential = UserCredential.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode("password"))
                        .roles(List.of(roleService.getOrSave(ERole.ROLE_PARTICIPANT)))
                        .build();
                Participant buildParticipant = Participant.builder()
                        .name(request.getName())
                        .gender(request.getGender())
                        .address(request.getAddress())
                        .phoneNumber(request.getPhoneNumber())
                        .userCredential(builUserCredential)
                        .build();
                particpantList.add(buildParticipant);
                userCredentialList.add(builUserCredential);
            }
        }
        userCredentialRepository.saveAll(userCredentialList);
        participantService.createList(particpantList);

        return userCredentialList.size();
    }
    private Set<UploadRequest> parseCsv(MultipartFile file) throws IOException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<UploadCsvRequest> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(UploadCsvRequest.class);
            CsvToBean<UploadCsvRequest> csvToBean =
                    new CsvToBeanBuilder<UploadCsvRequest>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> UploadRequest.builder()
                            .email(csvLine.getEmail())
                            .name(csvLine.getName())
                            .gender(csvLine.getGender())
                            .address(csvLine.getAddress())
                            .phoneNumber(csvLine.getPhoneNumber())
                            .build()
                    )
                    .collect(Collectors.toSet());
        }
    }
}
