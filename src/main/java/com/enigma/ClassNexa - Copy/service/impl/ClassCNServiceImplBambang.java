package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.repository.ClassCNRepositoryBambang;
import com.enigma.ClassNexa.service.ClassCNServiceBambang;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassCNServiceImplBambang implements ClassCNServiceBambang {
    private final ClassCNRepositoryBambang classCNRepository;


    @Override
    public Classes getById(String id) {
        Optional<Classes> byId = classCNRepository.findById(id);
        return byId.get();
    }
}
