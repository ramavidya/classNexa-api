package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Admin;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.*;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.repository.AdminRepository;
import com.enigma.ClassNexa.service.AdminService;
import com.enigma.ClassNexa.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<Admin> getAll(SearchUserRequest request) {
        Specification<Admin> specification = getAdminSpecification(request);
        if (request.getPage() <= 0) request.setPage(1);
        if (request.getSize() <= 0) request.setSize(10);
        Pageable pageRequest = PageRequest.of(request.getPage()-1, request.getSize());
        Page<Admin> getAllAdmin = adminRepository.findAll(specification, pageRequest);
        return getAllAdmin;
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
    public UserResponse update(ProfileUpdateRequest request) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updatePassword(UpdatePasswordRequest request) {
        UserResponse admin = getById(request.getId());
        String updateUsercredential = userService.update(
                UserUpdateRequest.builder()
                        .email(admin.getEmail())
                        .password(request.getPassword())
                        .new_password(request.getNew_password())
                        .build()
        );
        return updateUsercredential;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(String id) {
        UserResponse admin = getById(id);
        UserCredential userCredential =(UserCredential) userService.loadUserByUsername(admin.getEmail());
        adminRepository.deleteById(admin.getId());
        String delete = userService.delete(userCredential);
        return delete;
    }

    private static Specification<Admin> getAdminSpecification(SearchUserRequest request) {
        Specification<Admin> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null){
                Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" +request.getName() + "%");
                predicates.add(namePredicate);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        return specification;
    }
}
