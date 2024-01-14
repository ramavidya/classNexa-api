package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.entity.Questions;
import com.enigma.ClassNexa.model.request.QuestionsRequest;
import com.enigma.ClassNexa.model.request.SearchQuestionsRequest;
import com.enigma.ClassNexa.model.request.UpdateStatusRequest;
import com.enigma.ClassNexa.model.response.QuestionsResponse;
import org.springframework.data.domain.Page;

public interface QuestionsService {

    Questions getById(String id);

    QuestionsResponse create(QuestionsRequest request);

    QuestionsResponse getOne(String id);

    QuestionsResponse update(UpdateStatusRequest request);

    Page<QuestionsResponse> getAll(SearchQuestionsRequest request);

    void deleteById(String id);


}
