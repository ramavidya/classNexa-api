package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.repository.TrainerRepository;
import com.enigma.ClassNexa.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    public Trainer getById(String id) {
        Optional<Trainer> optionalParticipant = trainerRepository.findById(id);

        if (optionalParticipant.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer Not Found");

        Trainer participant = optionalParticipant.get();
        return participant;
    }
}
