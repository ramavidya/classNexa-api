package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.RegisterRequest;
import com.enigma.ClassNexa.model.response.RegisterResponse;


public interface AuthService {
    RegisterResponse registerAdmin(RegisterRequest request);
    RegisterResponse registerTrainer(RegisterRequest request);
    RegisterResponse registerParticipant(RegisterRequest request);
    String login(LoginRequest request);

}
