package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.response.UserResponse;

import java.util.List;

public interface ParticipantService {
    String create(UserCreateRequest request);
    void createList(List<Participant> participants);
    List<UserResponse> getAll();
    UserResponse getById(String request);
    UserResponse update(ProfileUpdateRequest request);
    String updatePassword(UpdatePasswordRequest request);
    String delete(String id);
}
