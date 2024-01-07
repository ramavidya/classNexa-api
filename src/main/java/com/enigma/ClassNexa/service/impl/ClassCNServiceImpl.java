package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.repository.ClassCNRepository;
import com.enigma.ClassNexa.service.ClassCNService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassCNServiceImpl implements ClassCNService {
    private final ClassCNRepository classCNRepository;


    @Override
    public Classes getById(String id) {
        Optional<Classes> byId = classCNRepository.findById(id);
        return byId.get();
    }
}
