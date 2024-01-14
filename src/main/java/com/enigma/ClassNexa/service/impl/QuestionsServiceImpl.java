package com.enigma.ClassNexa.service.impl;


import com.enigma.ClassNexa.entity.*;
import com.enigma.ClassNexa.model.request.QuestionsRequest;
import com.enigma.ClassNexa.model.request.SearchQuestionsRequest;
import com.enigma.ClassNexa.model.request.UpdateStatusRequest;
import com.enigma.ClassNexa.model.response.ParticipantQuestionsResponse;
import com.enigma.ClassNexa.model.response.QuestionsResponse;
import com.enigma.ClassNexa.repository.DetailClassParticipantRepository;
import com.enigma.ClassNexa.repository.QuestionsRepository;
import com.enigma.ClassNexa.service.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestionsServiceImpl implements QuestionsService {

        private final QuestionsRepository questionsRepository;

        private final ParticipantService participantService;

        private final ScheduleService scheduleService;

        private final DetailClassParticipantRepository detailClassParticipantRepository;

        private final UserService userService;

        private final SendEmailService sendEmailService;

        @Override
        @Transactional(readOnly = true)
        public Questions getById(String id) {
                Questions questions = questionsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Questions not found"));
                return questions;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public QuestionsResponse create(QuestionsRequest request) {

                Schedule schedule = scheduleService.getByIdSchedule(request.getScheduleId());

                UserCredential principal =(UserCredential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Participant participant1 = participantService.getByUserCredential(principal);

                Questions questions = Questions.builder()
                        .question(request.getQuestion())
                        .course(request.getCourse())
                        .chapter(request.getChapter())
                        .status(request.isStatus())
                        .participant(participant1)
                        .schedule(schedule)
                        .build();

                List<DetailClassParticipant> byClassId = detailClassParticipantRepository.findByClassesId(schedule.getClasses_id().getId());
                for (int i=0;i<byClassId.size();i++){
                        Participant byParticipantId = participantService.getByParticipantId(byClassId.get(i).getParticipant().getId());
                        UserCredential userCredential = userService.loadUserById(byParticipantId.getUserCredential().getId());

                        String subject = "Selamat Datang di ClassNexa";
                        String message = "Pertanyaan anda sudah kami terima"+ byParticipantId.getName();
                        sendEmailService.sendEmail(userCredential.getEmail(), subject, message);
                }
                return toParticipantQuestionsResponse(questions);
        }

        @Override
        @Transactional(readOnly = true)
        public QuestionsResponse getOne(String id) {
                Optional<Questions> optionalQuestions = questionsRepository.findById(id);
                if (optionalQuestions.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Question Not Found");

                return toParticipantQuestionsResponse(optionalQuestions.get());
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public QuestionsResponse update(UpdateStatusRequest request) {
                Optional<Questions> optionalQuestions = questionsRepository.findById(request.getQuestionsId());
                if (optionalQuestions.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Question Not Found");

                Schedule schedule = scheduleService.getByIdSchedule(request.getScheduleId());

                Questions updateQuestion = optionalQuestions.get();
                updateQuestion.setStatus(request.isStatus() == false);

                Questions questions = questionsRepository.save(updateQuestion);

                List<DetailClassParticipant> byClassId = detailClassParticipantRepository.findByClassesId(schedule.getClasses_id().getId());
                for (int i=0;i<byClassId.size();i++){
                        Participant byParticipantId = participantService.getByParticipantId(byClassId.get(i).getParticipant().getId());
                        UserCredential userCredential = userService.loadUserById(byParticipantId.getUserCredential().getId());

                        String subject = "Selamat Datang di ClassNexa";
                        String message = "Pertanyaan anda sudah terjawab"+ byParticipantId.getName();
                        sendEmailService.sendEmail(userCredential.getEmail(), subject, message);
                }
                return toParticipantQuestionsResponse(questions);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public void deleteById(String id) {
                Questions questions = questionsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Questions not found"));
                questionsRepository.delete(questions);
        }


        @Override
        @Transactional(readOnly = true)
        public Page<QuestionsResponse> getAll(SearchQuestionsRequest request) {
                if (request.getPage() <= 0) request.setPage(1);
                Pageable pageable = PageRequest.of(
                        (request.getPage() - 1), request.getSize()
                );


                Specification<Questions> specification = getQuestionsSpesification(request);


                Page<Questions> questionsPage = questionsRepository.findAll(specification, pageable);


                return new PageImpl<>(questionsPage.getContent().stream()
                        .map(this::toParticipantQuestionsResponse).collect(Collectors.toList()),
                        pageable, questionsPage.getTotalElements());

        }

        private QuestionsResponse toParticipantQuestionsResponse(Questions questions) {

                Participant participant = participantService.getByParticipantId(questions.getParticipant().getId());

                Schedule schedule = scheduleService.getByIdSchedule(questions.getSchedule().getId());


                List<ParticipantQuestionsResponse> questionsResponses = new ArrayList<>();


                for (Questions questionsParticipant : participant.getQuestions()){

                        String status;
                        if (questionsParticipant.isStatus() == true){
                                status = "The question has been answered";
                        }else {
                                status = "Unanswered question";
                        }

                        ParticipantQuestionsResponse questionsResponse = ParticipantQuestionsResponse.builder()
                                .participantName(questionsParticipant.getParticipant().getName())
                                .question(questionsParticipant.getQuestion())
                                .chapter(questionsParticipant.getChapter())
                                .course(questionsParticipant.getCourse())
                                .status(status)
                                .build();
                        questionsResponses.add(questionsResponse);
                }

                QuestionsResponse trainerQuestionsResponse = QuestionsResponse.builder()
                        .questionsId(questions.getId())
                        .className(schedule.getClasses_id().getName())
                        .trainerName(schedule.getClasses_id().getTrainer().getName())
                        .participantQuestions(questionsResponses)
                        .startClasses(schedule.getStart_class())
                        .endClasses(schedule.getEnd_class())
                        .build();

                return trainerQuestionsResponse;
        }

        private Specification<Questions> getQuestionsSpesification(SearchQuestionsRequest request) {

                Specification<Questions> specification = (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        if (request.getParticipantName() != null){
                                Join<Questions, Participant> scheduleJoin = root.join("participant", JoinType.INNER);
                                Predicate participantNamePredicate = criteriaBuilder.like(
                                        scheduleJoin.get("name"), "%" + request.getParticipantName() + "%"
                                );
                                predicates.add(participantNamePredicate);
                        }

                        if (request.getClassesName() != null) {
                                Join<Questions, Schedule> scheduleJoin = root.join("schedule", JoinType.INNER);
                                Join<Schedule, Classes> classesJoin = scheduleJoin.join("classes_id", JoinType.INNER);
                                Predicate classNamePredicate = criteriaBuilder.like(
                                        classesJoin.get("name"), "%" + request.getClassesName() + "%"
                                );
                                predicates.add(classNamePredicate);
                        }

                        if (request.getTrainerName() != null) {
                                Join<Questions, Schedule> scheduleJoin = root.join("schedule", JoinType.INNER);
                                Join<Schedule, Classes> classesJoin = scheduleJoin.join("classes_id", JoinType.INNER);
                                Join<Classes, Trainer> trainerJoin = classesJoin.join("trainer", JoinType.INNER);
                                Predicate trainerNamePredicate = criteriaBuilder.like(
                                        trainerJoin.get("name"), "%" + request.getTrainerName() + "%"
                                );
                                predicates.add(trainerNamePredicate);
                        }

                        if (request.getStart_class() != null) {
                                Join<Questions, Schedule> scheduleJoin = root.join("schedule", JoinType.INNER);
                                Predicate start_class = criteriaBuilder.equal(
                                        scheduleJoin.get("start_class"), request.getStart_class()
                                );
                                predicates.add(start_class);

                        }


                        if (request.isStatus()){
                                Predicate namePredicate = criteriaBuilder.isTrue(
                                        root.get("status")
                                );
                                predicates.add(namePredicate);
                        }


                        return query.where(predicates.toArray(new Predicate[] {} )).getRestriction();

                };
                return specification;
        }



}
