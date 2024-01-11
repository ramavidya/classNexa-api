package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.repository.ParticipantRepositoryRama;
import com.enigma.ClassNexa.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepositoryRama participantRepositoryRama;
    @Override
    public Participant getByParticipantId(String id) {
        Optional<Participant> optionalParticipant = participantRepositoryRama.findById(id);
        if (optionalParticipant.isEmpty()) throw new RuntimeException("not found");
        return optionalParticipant.get();
    }

    @Override
    public List<Participant> getAllParticipant() {
        return participantRepositoryRama.findAll();
    }

    @Override
    public List<Participant> getParticipantsByNames(List<String> name) {
        return participantRepositoryRama.findByNameIn(name);
    }


}
