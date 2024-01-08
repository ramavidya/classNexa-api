package com.enigma.ClassNexa.controller;

import com.Api.ClassNexa.model.*;
import com.Api.ClassNexa.service.Questions_Status_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/status")
public class Questions_Status_Controller {
    private final Questions_Status_Service questions_status_service;


    @PostMapping
    public ResponseEntity<?> createQuestionsStatus(@RequestBody Question_Status_Request request) {
        Questions_Status_Response questions = questions_status_service.create(request);
        WebResponse<Questions_Status_Response> response = WebResponse.<Questions_Status_Response>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new Status")
                .data(questions)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllStatus(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size

    ) {

        Question_Status_Request request = Question_Status_Request.builder()
                .page(page)
                .size(size)
                .build();

        Page<Questions_Status_Response> responses = questions_status_service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPage(responses.getTotalPages())
                .totalElements(responses.getTotalElements())
                .build();

        WebResponse<List<Questions_Status_Response>> response = WebResponse.<List<Questions_Status_Response>>builder()
                .message("successfully get all Status")
                .status(HttpStatus.OK.getReasonPhrase())
                .pagging(pagingResponse)
                .data(responses.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionsById(@PathVariable String id) {
        questions_status_service.deleteById(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .message("succesfully update by id")
                .status(HttpStatus.OK.getReasonPhrase())
                .data("OK")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
