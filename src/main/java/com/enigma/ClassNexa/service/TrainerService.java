package com.enigma.ClassNexa.service;

import com.enigma.classnexa.model.request.UserCreateRequest;
import com.enigma.classnexa.model.request.UserUpdateRequest;
import com.enigma.classnexa.model.response.UserResponse;

import java.util.List;

public interface TrainerService {
    String create(UserCreateRequest request);
    List<UserResponse> getAll();
    UserResponse getById(String request);

    UserResponse update(UserUpdateRequest request);

}
