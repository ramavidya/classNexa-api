package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.model.request.SearchUserRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.request.UserCreateRequest;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParticipantService {
    String create(UserCreateRequest request);
    Page<Participant> getAll(SearchUserRequest request);
    UserResponse getById(String request);
    UserResponse update(ProfileUpdateRequest request);
    String updatePassword(UpdatePasswordRequest request);
    String delete(String id);
    Participant getByParticipantId(String participantId);
}
