package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.model.request.ClassesRequest;
import com.enigma.ClassNexa.model.request.SearchDetailClassRequest;
import com.enigma.ClassNexa.model.request.UpdateClassesRequest;
import com.enigma.ClassNexa.model.response.ClassResponse;
import org.springframework.data.domain.Page;

public interface ClassesService {

    ClassResponse create (ClassesRequest request);
    ClassResponse update(UpdateClassesRequest request);
    void deleteById (String id);
    Classes getId(String id);
    ClassResponse getById(String id);
    Page<ClassResponse> getAll(SearchDetailClassRequest response);
    ClassResponse deleteDetailParticipant(UpdateClassesRequest request);


}
