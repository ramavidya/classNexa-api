package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.model.request.QuestionStatusRequest;
import com.enigma.ClassNexa.model.request.SearchStatusRequest;
import com.enigma.ClassNexa.model.response.QuestionsStatusResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.QuestionsStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/status")
public class QuestionsStatusController {
    private final QuestionsStatusService questions_status_service;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createQuestionsStatus(@RequestBody QuestionStatusRequest request) {
        QuestionsStatusResponse questions = questions_status_service.create(request);
        WebResponse<QuestionsStatusResponse> response = WebResponse.<QuestionsStatusResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new Status")
                .data(questions)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllStatus(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size

    ) {

        SearchStatusRequest request = SearchStatusRequest.builder()
                .page(page)
                .size(size)
                .build();

        Page<QuestionsStatusResponse> responses = questions_status_service.getAll(request);


        WebResponse<List<QuestionsStatusResponse>> response = WebResponse.<List<QuestionsStatusResponse>>builder()
                .message("successfully get all Status")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(responses.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
//    @PreAuthorize("hasRole('ADMIN')")
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
