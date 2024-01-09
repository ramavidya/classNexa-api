package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.repository.ParticipantRepository;
import com.enigma.ClassNexa.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository repository;

    @Override
    public Participant getByParticipantId(String id) {
        return repository.findById(id).orElse(null);

    }

    @Override
    public Participant create(Participant participant) {
        return repository.save(participant);
    }
}
