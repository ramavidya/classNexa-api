package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.ParticipantRama;

import java.util.List;

public interface ParticipantServiceRama {
    ParticipantRama getByParticipantId (String id);

    List<ParticipantRama> getAllParticipant ();
    List<ParticipantRama> getParticipantsByNames(List<String> participantNames);
}
