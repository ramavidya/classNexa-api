package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.entity.Participant;

public interface ParticipantService {

    Participant getByParticipantId(String id);
    Participant create(Participant participant);

}
