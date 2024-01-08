package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.model.request.UpdateClassesRequest;
import com.enigma.ClassNexa.repository.ClassesRepository;
import com.enigma.ClassNexa.service.ClassesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassesServiceImpl implements ClassesService {
    private final ClassesRepository classesRepository;
    @Override
    public Classes getClassById(String id) {
        Optional<Classes> optionalClasses = classesRepository.findById(id);
        if (optionalClasses.isEmpty()) throw new RuntimeException("not found");
        return optionalClasses.get();
    }

    @Override
    public Classes updateClasses(UpdateClassesRequest request) {
        return null;
    }
}
