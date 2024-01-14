package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.UserCredential;

import com.enigma.ClassNexa.model.request.SearchUserRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.response.UserResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface TrainerService {
    String create(UserCreateRequest request) throws IOException;
    Page<Trainer> getAll(SearchUserRequest request);
    UserResponse getById(String request);
    UserResponse update(ProfileUpdateRequest request);
    String updatePassword(UpdatePasswordRequest request);
    String delete(String id);
    Trainer getByTrainerId(String id);
    Trainer getByUserCredential(UserCredential userCredential);
}
