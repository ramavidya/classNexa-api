package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Admin;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.model.request.UserUpdateRequest;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.repository.AdminRepository;
import com.enigma.ClassNexa.service.AdminService;
import com.enigma.ClassNexa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(UserCreateRequest request) {

        Admin buildAdmin = Admin.builder()
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(request.getUserCredential())
                .build();

        Admin admin = adminRepository.saveAndFlush(buildAdmin);
        return admin.getName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserResponse> getAll() {
        List<Admin> findAll = adminRepository.findAll();

        List<UserResponse> userGetResponses = new ArrayList<>();
        for (Admin admin : findAll) {
            UserResponse buildResponse = UserResponse.builder()
                    .id(admin.getId())
                    .name(admin.getName())
                    .gender(admin.getGender())
                    .address(admin.getAddress())
                    .email(admin.getUserCredential().getEmail())
                    .phoneNumber(admin.getPhoneNumber())
                    .build();

            userGetResponses.add(buildResponse);
        }
        return userGetResponses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse getById(String request) {
        Optional<Admin> optionalAdmin = adminRepository.findById(request);
        if (!optionalAdmin.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found");

        return UserResponse.builder()
                .id(optionalAdmin.get().getId())
                .name(optionalAdmin.get().getName())
                .gender(optionalAdmin.get().getGender())
                .address(optionalAdmin.get().getAddress())
                .email(optionalAdmin.get().getUserCredential().getEmail())
                .phoneNumber(optionalAdmin.get().getPhoneNumber())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(UserUpdateRequest request) {
        UserResponse findId = getById(request.getId());
        UserCredential userCredential =(UserCredential) userService.loadUserByUsername(findId.getEmail());

        Admin buildAdmin = Admin.builder()
                .id(findId.getId())
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(userCredential)
                .build();

        Admin admin = adminRepository.saveAndFlush(buildAdmin);

        return UserResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .gender(admin.getGender())
                .address(admin.getAddress())
                .email(admin.getUserCredential().getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }
}
