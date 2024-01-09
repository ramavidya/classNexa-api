package com.enigma.ClassNexa.service;


import com.enigma.ClassNexa.model.Request.ClassesRequest;
import com.enigma.ClassNexa.model.Request.SearchDetailClassRequest;
import com.enigma.ClassNexa.model.Request.UpdateClassesRequest;
import com.enigma.ClassNexa.model.Response.ClassResponse;
import com.enigma.ClassNexa.entity.Classes;
import org.springframework.data.domain.Page;

public interface ClassesService {

    ClassResponse create (ClassesRequest request);
    ClassResponse update(UpdateClassesRequest request);
    void deleteById (String id);
    Classes getId(String id);
    ClassResponse getById(String id);
    Page<ClassResponse> getAll(SearchDetailClassRequest response);


}
