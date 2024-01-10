package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.RegisterRequest;
import com.enigma.ClassNexa.model.response.RegisterResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface AuthService {
    RegisterResponse registerAdmin(RegisterRequest request);
    RegisterResponse registerTrainer(RegisterRequest request);
    RegisterResponse registerParticipant(RegisterRequest request);
    Integer uploadParticipant(MultipartFile file) throws IOException;
    String login(LoginRequest request);

}
