package com.enigma.ClassNexa.service.impl;


import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.entity.Questions;
import com.enigma.ClassNexa.entity.Questions_Status;
import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.model.QuestionsRequest;
import com.enigma.ClassNexa.model.QuestionsResponse;
import com.enigma.ClassNexa.model.SearchQuestionsRequest;
import com.enigma.ClassNexa.repository.ParticipantRepository;
import com.enigma.ClassNexa.repository.QuestionsRepository;
import com.enigma.ClassNexa.repository.ScheduleRepository;
import com.enigma.ClassNexa.service.QuestionsService;
import com.enigma.ClassNexa.service.Questions_Status_Service;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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


@Service
@RequiredArgsConstructor
public class QuestionsServiceImpl implements QuestionsService {

        private final QuestionsRepository questionsRepository;

        private final ParticipantRepository participantRepository;

        private final ScheduleRepository scheduleRepository;

        private final Questions_Status_Service questions_status_service;

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

                Questions_Status questions_status = questions_status_service.getById(request.getStatus().getId());


                Questions questions = Questions.builder()
                        .question(request.getQuestion())
                        .course(request.getCourse())
                        .chapter(request.getChapter())
                        .questions_status(questions_status)
                        .participant(optionalParticipant.get())
                        .schedule(optionalSchedule.get())
                        .build();



                questionsRepository.saveAndFlush(questions);


                return toQuestionsResponse(questions);
        }

        @Override
        @Transactional(readOnly = true)
        public QuestionsResponse getOne(String id) {
                Optional<Questions> optionalQuestions = questionsRepository.findById(id);
                if (optionalQuestions.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Question Not Found");

                return toQuestionsResponse(optionalQuestions.get());

        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public QuestionsResponse update(QuestionsRequest request) {
                Optional<Questions> optionalQuestions = questionsRepository.findById(request.getId());
                if (optionalQuestions.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Question Not Found");

                Questions_Status questions_status = questions_status_service.getById(request.getStatus().getId());

                Questions updateQuestion = optionalQuestions.get();
                updateQuestion.setQuestions_status(questions_status);

                return toQuestionsResponse(questionsRepository.save(updateQuestion));

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



                return questionsPage.map(this::toQuestionsResponse);
        }

        private Specification<Questions> getQuestionsSpesification(SearchQuestionsRequest request) {

                Specification<Questions> specification = (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        if (request.getParticipantName() != null){
                                Join<Questions, Participant> scheduleJoin = root.join("participant", JoinType.INNER);
                                Predicate trainerNamePredicate = criteriaBuilder.like(
                                        scheduleJoin.get("name"), "%" + request.getParticipantName() + "%"
                                );
                                predicates.add(trainerNamePredicate);
                        }

//                        if (request.getClasseName() != null){
//                                Join<Questions, Schedule> scheduleJoin = root.join("schedule", JoinType.INNER);
//                                Predicate trainerNamePredicate = criteriaBuilder.like(
//                                        scheduleJoin.get("name"), "%" + request.getClasseName() + "%"
//                                );
//                                predicates.add(trainerNamePredicate);
//                        }



                        return query.where(predicates.toArray(new Predicate[] {} )).getRestriction();

                };
                return specification;
        }




        @Override
        @Transactional(rollbackFor = Exception.class)
        public void deleteById(String id) {
                Questions questions = questionsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Questions not found"));
                questionsRepository.delete(questions);
        }


        private QuestionsResponse toQuestionsResponse(Questions questions) {

                Optional<Participant> optionalParticipant = participantRepository.findById(questions.getParticipant().getId());
                if (optionalParticipant.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found for the question");
                }

                Optional<Schedule> optionalSchedule = scheduleRepository.findById(questions.getSchedule().getId());
                if (optionalSchedule.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found for the question");
                }

                Questions_Status questions_status = questions_status_service.getById(questions.getQuestions_status().getId());




                String status;
                if (questions_status.isStatus()){
                        status = "The question has been answered";
                }else {
                        status = "Unanswered question";
                }

                QuestionsResponse response = QuestionsResponse.builder()
                        .id(questions.getId())
                        .className(optionalSchedule.get().getClasses().getName())
                        .trainerName(optionalSchedule.get().getClasses().getTrainer().getName())
                        .participant_name(optionalParticipant.get().getName())
                        .question(questions.getQuestion())
                        .course(questions.getCourse())
                        .chapter(questions.getChapter())
                        .status(status)
                        .start_clases(optionalSchedule.get().getStart_class().toLocalDateTime())
                        .end_classes(optionalSchedule.get().getEnd_class().toLocalDateTime())
                        .build();

                return response;
        }



}
