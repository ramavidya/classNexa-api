package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.repository.TrainerRepository;
import com.enigma.ClassNexa.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    @Override
    public Trainer getById(String id) {
        Optional<Trainer> byId = trainerRepository.findById(id);
        return byId.get();
    }
}
