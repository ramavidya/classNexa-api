package com.enigma.ClassNexa.service.impl;

import com.enigma.classnexa.constan.ERole;
import com.enigma.classnexa.entity.Role;
import com.enigma.classnexa.entity.UserCredential;
import com.enigma.classnexa.model.request.LoginRequest;
import com.enigma.classnexa.model.request.RegisterRequest;
import com.enigma.classnexa.model.request.UserCreateRequest;
import com.enigma.classnexa.model.response.RegisterResponse;
import com.enigma.classnexa.repository.UserCredentialRepository;
import com.enigma.classnexa.security.JwtUtil;
import com.enigma.classnexa.service.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
    public RegisterResponse registerTrainer(RegisterRequest request) {
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
    public RegisterResponse registerParticipant(RegisterRequest request) {
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


}
