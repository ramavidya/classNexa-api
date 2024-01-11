package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.ParticipantRama;
import com.enigma.ClassNexa.repository.ParticipantRepositoryRama;
import com.enigma.ClassNexa.service.ParticipantServiceRama;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceRamaImpl implements ParticipantServiceRama {
    private final ParticipantRepositoryRama participantRepositoryRama;
    @Override
    public ParticipantRama getByParticipantId(String id) {
        Optional<ParticipantRama> optionalParticipant = participantRepositoryRama.findById(id);
        if (optionalParticipant.isEmpty()) throw new RuntimeException("not found");
        return optionalParticipant.get();
    }

    @Override
    public List<ParticipantRama> getAllParticipant() {
        return participantRepositoryRama.findAll();
    }

    @Override
    public List<ParticipantRama> getParticipantsByNames(List<String> name) {
        return participantRepositoryRama.findByNameIn(name);
    }


}
