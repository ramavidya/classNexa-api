package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.*;
import com.enigma.ClassNexa.model.JwtClaim;
import com.enigma.ClassNexa.model.request.SearchTrainerNotesRequest;
import com.enigma.ClassNexa.model.request.TrainerNotesRequest;
import com.enigma.ClassNexa.model.response.TrainerNotesResponse;
import com.enigma.ClassNexa.repository.TrainerNotesRepository;
import com.enigma.ClassNexa.security.JwtAuthenticationFilter;
import com.enigma.ClassNexa.security.JwtUtil;
import com.enigma.ClassNexa.service.ClassesService;
import com.enigma.ClassNexa.service.ScheduleService;
import com.enigma.ClassNexa.service.TrainerNotesService;
import com.enigma.ClassNexa.service.TrainerService;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerNotesServiceImpl implements TrainerNotesService {
    private final TrainerNotesRepository trainerNotesRepository;
    private final TrainerService trainerService;
    private final ScheduleService scheduleService;
    private final ClassesService classesService;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private TrainerNotesResponse toTrainerNotesResponse(TrainerNotes notes){
//        scheduleService.getByIdSchedule(notes.getSchedule().getId())
        log.info(notes.getSchedule().getId());
        return TrainerNotesResponse.builder()
                .id(notes.getId())
                .notes(notes.getNotes())
                .trainer_id(notes.getTrainer().getId())
                .trainer(notes.getTrainer().getName())
                .schedule_id(notes.getSchedule().getId())
                .start_class(notes.getSchedule().getStart_class().toString())
                .end_class(notes.getSchedule().getEnd_class().toString())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TrainerNotesResponse create(TrainerNotesRequest request) {
        Schedule byIdSchedule = scheduleService.getByIdSchedule(request.getSchedule());

        UserCredential userCredential = (UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trainer byUserCredential = trainerService.getByUserCredential(userCredential);
        log.info(byUserCredential.getName());

//        String string = jwtAuthenticationFilter.parseJTW(servletRequest);
//        JwtClaim userInfoByToken = jwtUtil.getUserInfoByToken();
//        log.info(userInfoByToken.getUserId());

        TrainerNotes trainerNotes =TrainerNotes.builder()
                .notes(request.getNotes())
                .trainer(byUserCredential)
                .schedule(byIdSchedule)
                .build();
        TrainerNotes save = trainerNotesRepository.save(trainerNotes);
        return toTrainerNotesResponse(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TrainerNotesResponse> getAll() {
        List<TrainerNotes> all = trainerNotesRepository.findAll();
        List<TrainerNotesResponse> trainerNotes = new ArrayList<>();
        for (int i=0;i< all.size();i++){
            TrainerNotes notes = TrainerNotes.builder()
                    .id(all.get(i).getId())
                    .notes(all.get(i).getNotes())
                    .trainer(all.get(i).getTrainer())
                    .schedule(all.get(i).getSchedule())
                    .build();
            TrainerNotesResponse trainerNotesResponse = toTrainerNotesResponse(notes);

            trainerNotes.add(trainerNotesResponse);
        }
        return trainerNotes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TrainerNotesResponse getById(String id) {
        Optional<TrainerNotes> byId = trainerNotesRepository.findById(id);
        TrainerNotes trainerNotes = TrainerNotes.builder()
                .id(byId.get().getId())
                .notes(byId.get().getNotes())
                .trainer(byId.get().getTrainer())
                .schedule(byId.get().getSchedule())
                .build();
        return toTrainerNotesResponse(trainerNotes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TrainerNotesResponse update(TrainerNotesRequest trainerNotes) {
        Optional<TrainerNotes> byId = trainerNotesRepository.findById(trainerNotes.getId());
        Trainer byId1 = trainerService.getByTrainerId(trainerNotes.getTrainer());
        Schedule byIdSchedule = scheduleService.getByIdSchedule(trainerNotes.getSchedule());

        TrainerNotes notes = TrainerNotes.builder()
                .id(byId.get().getId())
                .notes(trainerNotes.getNotes())
                .trainer(byId1)
                .schedule(byIdSchedule)
                .build();
        TrainerNotes save = trainerNotesRepository.save(notes);

        return toTrainerNotesResponse(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(String id) {
        trainerNotesRepository.deleteById(id);
        return "ok";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<TrainerNotes> getAllTrainerNotes(SearchTrainerNotesRequest request) {
        if (request.getPage()<=0)request.setPage(1);
        PageRequest pageable = PageRequest.of(request.getPage()-1, request.getSize());
        Specification<TrainerNotes> productSpecification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getClasses()!= null){
                Classes byId = classesService.getId(request.getClasses());
                Predicate namePredicate = criteriaBuilder.equal(root.get("trainer"), byId.getTrainer());
                predicates.add(namePredicate);
            }
            if (request.getSchedule()!= null){
                Schedule byIdSchedule = scheduleService.getByIdSchedule(request.getSchedule());
                Predicate namePredicate = criteriaBuilder.equal(root.get("schedule"), byIdSchedule);
                predicates.add(namePredicate);
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        return trainerNotesRepository.findAll(productSpecification, pageable);
    }


}
