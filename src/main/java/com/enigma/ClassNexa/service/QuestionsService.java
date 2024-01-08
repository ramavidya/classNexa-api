package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.entity.Questions;
import com.enigma.ClassNexa.model.QuestionsRequest;
import com.enigma.ClassNexa.model.QuestionsResponse;
import com.enigma.ClassNexa.model.SearchQuestionsRequest;
import org.springframework.data.domain.Page;

public interface QuestionsService {

    Questions getById(String id);

    QuestionsResponse create(QuestionsRequest request);

    QuestionsResponse getOne(String id);

    QuestionsResponse update(QuestionsRequest request);

    Page<QuestionsResponse> getAll(SearchQuestionsRequest request);

    void deleteById(String id);


}
