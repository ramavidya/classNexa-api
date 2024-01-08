package com.enigma.ClassNexa.service;

import com.Api.ClassNexa.entity.Questions_Status;
import com.Api.ClassNexa.model.Question_Status_Request;
import com.Api.ClassNexa.model.Questions_Status_Response;
import org.springframework.data.domain.Page;

public interface Questions_Status_Service {

    Questions_Status getById(String id);

    Questions_Status_Response create(Question_Status_Request request);

    Page<Questions_Status_Response> getAll(Question_Status_Request request);

    void deleteById(String id);
}
