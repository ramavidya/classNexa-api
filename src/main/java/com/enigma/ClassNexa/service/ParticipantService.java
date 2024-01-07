package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.model.request.UserUpdateRequest;
import com.enigma.ClassNexa.model.response.UserResponse;

import java.util.List;

public interface ParticipantService {
    String create(UserCreateRequest request);
    List<UserResponse> getAll();
    UserResponse getById(String request);

    UserResponse update(UserUpdateRequest request);
}
