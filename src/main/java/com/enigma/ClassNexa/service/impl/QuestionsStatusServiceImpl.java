package com.enigma.ClassNexa.service.impl;


import com.enigma.ClassNexa.entity.Questions_Status;
import com.enigma.ClassNexa.model.request.QuestionStatusRequest;
import com.enigma.ClassNexa.model.request.SearchStatusRequest;
import com.enigma.ClassNexa.model.response.QuestionsStatusResponse;
import com.enigma.ClassNexa.repository.QuestionsStatusRepository;
import com.enigma.ClassNexa.service.QuestionsStatusService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class QuestionsStatusServiceImpl implements QuestionsStatusService {

    private final QuestionsStatusRepository questionsStatusRepository;

    @Override
    public Questions_Status getById(String id) {
        Questions_Status questions_status = questionsStatusRepository.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found"));
        return questions_status;
    }

    @Override
    public QuestionsStatusResponse create(QuestionStatusRequest request) {

        Questions_Status status = Questions_Status.builder()
                .status(request.isStatus())
                .build();


        Questions_Status savedStatus = questionsStatusRepository.save(status);


        QuestionsStatusResponse questions_status_response = QuestionsStatusResponse.builder()
                .id(savedStatus.getId())
                .status(savedStatus.isStatus())
                .build();

        return questions_status_response;
    }

    @Override
    public Page<QuestionsStatusResponse> getAll(SearchStatusRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(
                (request.getPage() - 1), request.getSize()
        );

        Page<Questions_Status> questionsPage = questionsStatusRepository.findAll(pageable);

        return questionsPage.map(status -> toResponse(status));
    }

    @Override
    public void deleteById(String id) {
        Questions_Status questions_status = questionsStatusRepository.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found"));
        questionsStatusRepository.delete(questions_status);
    }

    private QuestionsStatusResponse toResponse(Questions_Status status) {
        return QuestionsStatusResponse.builder()
                .id(status.getId())
                .status(status.isStatus())
                .build();
    }
}
