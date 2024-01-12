package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.entity.DetailClassParticipant;
import com.enigma.ClassNexa.model.request.ClassesRequest;
import com.enigma.ClassNexa.model.request.DetailClassParticipantRequest;
import com.enigma.ClassNexa.model.request.SearchDetailClassRequest;
import com.enigma.ClassNexa.model.request.UpdateClassesRequest;
import com.enigma.ClassNexa.model.response.ClassResponse;
import com.enigma.ClassNexa.repository.ClassesRepository;
import com.enigma.ClassNexa.service.ClassDetailService;
import com.enigma.ClassNexa.service.ClassesService;
import com.enigma.ClassNexa.service.ParticipantService;
import com.enigma.ClassNexa.service.TrainerService;
import com.enigma.ClassNexa.util.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassesServiceImpl implements ClassesService {

    private final ClassesRepository classesRepository;
    private final ValidationUtils validationUtils;
    private final ParticipantService participantService;
    private final TrainerService trainerService;
    private final ClassDetailService classDetailService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassResponse create(ClassesRequest request) {
        validationUtils.validate(request.getTrainerId());
        Trainer trainer = trainerService.getByTrainerId(request.getTrainerId());
        Classes classes = Classes.builder()
                .name(request.getName())
                .trainer(trainer)
                .build();
        classesRepository.saveAndFlush(classes);

        List<DetailClassParticipant> detailClassParticipants = new ArrayList<>();
        for (DetailClassParticipantRequest detailClassParticipantRequest : request.getParticipants()) {
            Participant participant = participantService.getByParticipantId(detailClassParticipantRequest.getId());
            DetailClassParticipant byParticipantId =
                    classDetailService.getByParticipantId(participant);
            if (byParticipantId == null) {
                DetailClassParticipant detailClassParticipant = DetailClassParticipant.builder()
                        .participant(participant)
                        .classes(classes)
                        .build();
                detailClassParticipants.add(classDetailService.createOrUpdate(detailClassParticipant));
            }

        }

        List<String> result = detailClassParticipants.stream().map(dcp ->
                dcp.getParticipant().getName()).collect(Collectors.toList());

        return ClassResponse.builder()
                .id(classes.getId())
                .classes(classes.getName())
                .trainer(classes.getTrainer().getName())
                .participant(result)
                .build();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassResponse update(UpdateClassesRequest request) {
        validationUtils.validate(request);

        Optional<Classes> classes = classesRepository.findById(request.getId());
        if (classes.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Class Room Not Found");
        Trainer trainer = trainerService.getByTrainerId(request.getTrainerId());
        Classes room = classes.get();
        room.setName(request.getName());
        room.setTrainer(trainer);
        List<DetailClassParticipant> detailClassParticipants = new ArrayList<>();
        for (DetailClassParticipantRequest detailClassParticipantRequest : request.getParticipants()) {
            Participant participant = participantService.getByParticipantId(detailClassParticipantRequest.getId());
            DetailClassParticipant byParticipantId =
                    classDetailService.getByParticipantId(participant);
            if (byParticipantId == null) {
                DetailClassParticipant detailClassParticipant = DetailClassParticipant.builder()
                        .participant(participant)
                        .classes(room)
                        .build();
                detailClassParticipants.add(classDetailService.createOrUpdate(detailClassParticipant));
            }

        }
        classesRepository.save(room);

        List<String> result = detailClassParticipants.stream().map(dcp ->
                dcp.getParticipant().getName()).collect(Collectors.toList());

        return ClassResponse.builder()
                .id(room.getId())
                .classes(room.getName())
                .trainer(room.getTrainer().getName())
                .participant(result)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        validationUtils.validate(id);
        Optional<Classes> classesOptional = classesRepository.findById(id);
        if (classesOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found");
        }
        classDetailService.deleteByClassId(id);
        classesRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Classes getId(String id) {
        validationUtils.validate(id);
        Optional<Classes> classes = classesRepository.findById(id);
        if (classes.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class Room Not Found");
        return classes.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassResponse getById(String id) {
        validationUtils.validate(id);
        Optional<Classes> classesOptional = classesRepository.findById(id);
        if (classesOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found");
        }
        Classes classes = classesOptional.get();
        List<DetailClassParticipant> detailClassParticipants = classDetailService.getByClassId(id);
        List<String> participantNames = detailClassParticipants.stream()
                .map(dcp -> dcp.getParticipant().getName())
                .collect(Collectors.toList());

        return ClassResponse.builder()
                .id(classes.getId())
                .classes(classes.getName())
                .trainer(classes.getTrainer().getName())
                .participant(participantNames)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<ClassResponse> getAll(SearchDetailClassRequest request) {
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), direction, request.getSortBy());

        Specification<Classes> spec = getClassesSpecification(request);
        Page<Classes> classesPage = classesRepository.findAll(spec, pageable);

        List<ClassResponse> classResponseList = new ArrayList<>();
        for (Classes classes : classesPage.getContent()) {
            Trainer trainer = classes.getTrainer();
            List<DetailClassParticipant> detailClassParticipants = classDetailService.getAllDetail();

            List<String> result = new ArrayList<>();
            for (DetailClassParticipant detailClassParticipant : detailClassParticipants) {
                Participant participant = detailClassParticipant.getParticipant();
                result.add(participant.getName());
            }

            ClassResponse classResponse = ClassResponse.builder()
                    .id(classes.getId())
                    .classes(classes.getName())
                    .trainer(trainer.getName())
                    .participant(result)
                    .build();

            classResponseList.add(classResponse);
        }

        return new PageImpl<>(classResponseList, pageable, classesPage.getTotalElements());
    }

    private static Specification<Classes> getClassesSpecification(SearchDetailClassRequest request) {
        Specification<Classes> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getClasses() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getClasses() + "%"));
            }
            if (request.getTrainer() != null) {
                predicates.add(criteriaBuilder.like(root.get("trainer").get("name"), "%" + request.getTrainer() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return spec;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClassResponse deleteDetailParticipant(UpdateClassesRequest request) {

        Optional<Classes> classes = classesRepository.findById(request.getId());
        if (classes.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Class Room Not Found");
        Trainer trainer = trainerService.getByTrainerId(request.getTrainerId());
        Classes room = classes.get();
        room.setName(request.getName());
        room.setTrainer(trainer);
        List<DetailClassParticipant> detailClassParticipants = new ArrayList<>();
        for (DetailClassParticipantRequest detailClassParticipantRequest : request.getParticipants()) {
            Participant participant = participantService.getByParticipantId(detailClassParticipantRequest.getId());
            DetailClassParticipant byParticipantId =
                    classDetailService.getByParticipantId(participant);
            if (byParticipantId != null) {
                classDetailService.deleteById(byParticipantId.getId());
            }

        }
        classesRepository.save(room);

        List<String> result = detailClassParticipants.stream().map(dcp ->
                dcp.getParticipant().getName()).collect(Collectors.toList());

        return ClassResponse.builder()
                .id(room.getId())
                .classes(room.getName())
                .trainer(room.getTrainer().getName())
                .participant(result)
                .build();
    }



}

