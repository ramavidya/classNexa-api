package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.UserCredential;
import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.model.request.UserUpdateRequest;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.repository.TrainerRepository;
import com.enigma.ClassNexa.service.TrainerService;
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
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(UserCreateRequest request) {

        Trainer buildTrainer = Trainer.builder()
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .userCredential(request.getUserCredential())
                .build();

        Trainer trainer = trainerRepository.saveAndFlush(buildTrainer);
        return trainer.getName();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserResponse> getAll() {
        List<Trainer> findAll = trainerRepository.findAll();

        List<UserResponse> userGetResponses = new ArrayList<>();
        for (Trainer trainer : findAll) {
            UserResponse buildResponse = UserResponse.builder()
                    .id(trainer.getId())
                    .name(trainer.getName())
                    .gender(trainer.getGender())
                    .address(trainer.getAddress())
                    .email(trainer.getUserCredential().getEmail())
                    .phoneNumber(trainer.getPhoneNumber())
                    .build();

            userGetResponses.add(buildResponse);
        }
        return userGetResponses;
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
    public UserResponse update(UserUpdateRequest request) {
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
}
