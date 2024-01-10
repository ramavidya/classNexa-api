package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.repository.ClassesRepositoryBambang;
import com.enigma.ClassNexa.service.ClassesServiceBambang;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassesServiceImplBambang implements ClassesServiceBambang {
    private final ClassesRepositoryBambang classCNRepository;


    @Override
    public Classes getById(String id) {
        Optional<Classes> byId = classCNRepository.findById(id);
        return byId.get();
    }
}
