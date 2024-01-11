package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Participant;

import java.util.List;

public interface ParticipantService {
    Participant getByParticipantId (String id);

    List<Participant> getAllParticipant ();
    List<Participant> getParticipantsByNames(List<String> participantNames);
}
