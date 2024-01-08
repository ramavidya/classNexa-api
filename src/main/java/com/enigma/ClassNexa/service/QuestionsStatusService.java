package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.entity.Questions_Status;
import com.enigma.ClassNexa.model.request.QuestionStatusRequest;
import com.enigma.ClassNexa.model.response.Questions_Status_Response;
import org.springframework.data.domain.Page;

public interface QuestionsStatusService {

    Questions_Status getById(String id);

    Questions_Status_Response create(QuestionStatusRequest request);

    Page<Questions_Status_Response> getAll(QuestionStatusRequest request);

    void deleteById(String id);
}
