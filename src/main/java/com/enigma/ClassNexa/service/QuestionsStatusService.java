package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.entity.Questions_Status;
import com.enigma.ClassNexa.model.request.QuestionStatusRequest;
import com.enigma.ClassNexa.model.request.SearchStatusRequest;
import com.enigma.ClassNexa.model.response.QuestionsStatusResponse;
import org.springframework.data.domain.Page;

public interface QuestionsStatusService {

    Questions_Status getById(String id);

    QuestionsStatusResponse create(QuestionStatusRequest request);

    Page<QuestionsStatusResponse> getAll(SearchStatusRequest request);

    void deleteById(String id);
}
