package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.*;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.repository.TrainerRepository;
import com.enigma.ClassNexa.service.RestTemplateService;
import com.enigma.ClassNexa.service.TrainerService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final RestTemplateService restTemplateService;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(UserCreateRequest request) throws IOException {
        Trainer buildTrainer = Trainer.builder()
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(request.getUserCredential())
                .build();
        Trainer trainer = trainerRepository.saveAndFlush(buildTrainer);

        if (request.getPhoneNumber() != null) {
            TargetNumberRequest buildTargetNumber = TargetNumberRequest.builder()
                    .number(List.of(trainer.getPhoneNumber()))
                    .build();
            restTemplateService.sendMessageRegisterWhatsapp(buildTargetNumber);
        }
        return trainer.getName();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<Trainer> getAll(SearchUserRequest request) {
        Specification<Trainer> specification = getTrainerSpecification(request);
        if (request.getPage() <= 0) request.setPage(1);
        if (request.getSize() <= 0) request.setSize(10);
        Pageable pageRequest = PageRequest.of(request.getPage()-1, request.getSize());
        Page<Trainer> findAll = trainerRepository.findAll(specification,pageRequest);
        return findAll;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse getById(String request) {
        Optional<Trainer> optionalTrainer = trainerRepository.findById(request);
        if (!optionalTrainer.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found");
        return UserResponse.builder()
                .id(optionalTrainer.get().getId())
                .name(optionalTrainer.get().getName())
                .gender(optionalTrainer.get().getGender())
                .address(optionalTrainer.get().getAddress())
                .email(optionalTrainer.get().getUserCredential().getEmail())
                .phoneNumber(optionalTrainer.get().getPhoneNumber())
                .build();
    }
    @Override
    public Trainer getByUserCredential(UserCredential userCredential) {
        return trainerRepository.findByUserCredential(userCredential).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "trainer not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(ProfileUpdateRequest request) {
        UserResponse findId = getById(request.getId());
        UserCredential userCredential =(UserCredential) userService.loadUserByUsername(findId.getEmail());
        Trainer buildTrainer = Trainer.builder()
                .id(findId.getId())
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(userCredential)
                .build();
        Trainer trainer = trainerRepository.saveAndFlush(buildTrainer);
        return UserResponse.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .gender(trainer.getGender())
                .address(trainer.getAddress())
                .email(trainer.getUserCredential().getEmail())
                .phoneNumber(trainer.getPhoneNumber())
                .build();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updatePassword(UpdatePasswordRequest request) {
        UserResponse trainer = getById(request.getId());
        String updateUsercredential = userService.update(
                UserUpdateRequest.builder()
                        .email(trainer.getEmail())
                        .password(request.getPassword())
                        .new_password(request.getNew_password())
                        .build()
        );
        return updateUsercredential;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(String id) {
        UserResponse trainer = getById(id);
        UserCredential userCredential =(UserCredential) userService.loadUserByUsername(trainer.getEmail());
        trainerRepository.deleteById(trainer.getId());
        String delete = userService.delete(userCredential);
        return delete;
    }
    @Override
    public Trainer getByTrainerId(String id) {
        Optional<Trainer> optionalParticipant = trainerRepository.findById(id);
        if (optionalParticipant.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer Not Found");
        Trainer Trainer = optionalParticipant.get();
        return Trainer;
    }
    private static Specification<Trainer> getTrainerSpecification(SearchUserRequest request) {
        Specification<Trainer> specification = (root, query, criteriaBuilder) -> {
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
