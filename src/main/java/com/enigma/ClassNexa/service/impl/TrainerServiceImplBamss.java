package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.repository.TrainerRepositoryBambang;
import com.enigma.ClassNexa.service.TrainerServicebamss;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImplBamss implements TrainerServicebamss {
    private final TrainerRepositoryBambang trainerRepository;
    @Override
    public Trainer getById(String id) {
        Optional<Trainer> byId = trainerRepository.findById(id);
        return byId.get();
    }
}
