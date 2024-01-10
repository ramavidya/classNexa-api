package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.model.request.TargetNumberRequest;

import java.io.IOException;

public interface RestTemplateService {
   String sendMessageRegisterParticipant(TargetNumberRequest request) throws IOException;
}
