package com.enigma.ClassNexa.service.impl;


import com.enigma.ClassNexa.entity.*;
import com.enigma.ClassNexa.model.request.QuestionsRequest;
import com.enigma.ClassNexa.model.request.UpdateStatusRequest;
import com.enigma.ClassNexa.model.response.ParticipantQuestionsResponse;
import com.enigma.ClassNexa.model.request.SearchQuestionsRequest;
import com.enigma.ClassNexa.model.response.QuestionsResponse;
import com.enigma.ClassNexa.repository.ParticipantRepository;
import com.enigma.ClassNexa.repository.QuestionsRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.QuestionsService;
import com.enigma.ClassNexa.service.QuestionsStatusService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class QuestionsServiceImpl implements QuestionsService {

        private final QuestionsRepository questionsRepository;

        private final ParticipantRepository participantRepository;

        private final ScheduleRepository scheduleRepository;

        private final QuestionsStatusService questions_status_service;

        @Override
        @Transactional(readOnly = true)
        public Questions getById(String id) {
                Questions questions = questionsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Questions not found"));
                return questions;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public QuestionsResponse create(QuestionsRequest request) {

                Optional<Participant> optionalParticipant = participantRepository.findById(request.getParticipantId());
                if (optionalParticipant.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found for the question");
                }

                Optional<Schedule> optionalSchedule = scheduleRepository.findById(request.getScheduleId());
                if (optionalSchedule.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found for the question");
                }

                Questions_Status questions_status = questions_status_service.getById(request.getStatusId());


                Questions questions = Questions.builder()
                        .question(request.getQuestion())
                        .course(request.getCourse())
                        .chapter(request.getChapter())
                        .questionsStatus(questions_status)
                        .participant(optionalParticipant.get())
                        .schedule(optionalSchedule.get())
                        .build();



                questionsRepository.saveAndFlush(questions);


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

                Optional<Schedule> optionalSchedule = scheduleRepository.findById(request.getScheduleId());
                if (optionalSchedule.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found for the question");
                }

                Questions_Status questions_status = questions_status_service.getById(request.getStatusId());

                Questions updateQuestion = optionalQuestions.get();
                updateQuestion.setQuestionsStatus(questions_status);

                return toParticipantQuestionsResponse(questionsRepository.save(updateQuestion));

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
                Optional<Participant> optionalParticipant = participantRepository.findById(questions.getParticipant().getId());
                if (optionalParticipant.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found for the question");
                }

                Optional<Schedule> optionalSchedule = scheduleRepository.findById(questions.getSchedule().getId());
                if (optionalSchedule.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found for the question");
                }


                List<ParticipantQuestionsResponse> questionsResponses = new ArrayList<>();


                for (Questions questionsParticipant : optionalParticipant.get().getQuestions()){

                        String status;
                        if (questionsParticipant.getQuestionsStatus().isStatus() == true){
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
                        .className(optionalSchedule.get().getClasses().getName())
                        .trainerName(optionalSchedule.get().getClasses().getTrainer().getName())
                        .participantQuestions(questionsResponses)
                        .startClasses(optionalSchedule.get().getStart_class().toLocalDateTime())
                        .endClasses(optionalSchedule.get().getEnd_class().toLocalDateTime())
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

                        if (request.getClasseName() != null) {
                                Join<Questions, Schedule> scheduleJoin = root.join("schedule", JoinType.INNER);
                                Join<Schedule, Classes> classesJoin = scheduleJoin.join("classes", JoinType.INNER);
                                Predicate classNamePredicate = criteriaBuilder.like(
                                        classesJoin.get("name"), "%" + request.getClasseName() + "%"
                                );
                                predicates.add(classNamePredicate);
                        }

                        if (request.getTrainerName() != null) {
                                Join<Questions, Schedule> scheduleJoin = root.join("schedule", JoinType.INNER);
                                Join<Schedule, Classes> classesJoin = scheduleJoin.join("classes", JoinType.INNER);
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

                        Join<Questions, Questions_Status> statusJoin = root.join("questionsStatus", JoinType.INNER);
                        if (request.isStatus()) {
                                Predicate statusPredicate = criteriaBuilder.isTrue(statusJoin.get("status"));
                                predicates.add(statusPredicate);
                        }




                        return query.where(predicates.toArray(new Predicate[] {} )).getRestriction();

                };
                return specification;
        }



}
