package com.enigma.ClassNexa.service;

import com.enigma.classnexa.model.request.LoginRequest;
import com.enigma.classnexa.model.request.RegisterRequest;
import com.enigma.classnexa.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerAdmin(RegisterRequest request);
    RegisterResponse registerTrainer(RegisterRequest request);
    RegisterResponse registerParticipant(RegisterRequest request);
    String login(LoginRequest request);
}
