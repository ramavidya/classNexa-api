package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.model.request.QuestionsRequest;
import com.enigma.ClassNexa.model.request.SearchQuestionsRequest;
import com.enigma.ClassNexa.model.response.PagingResponse;
import com.enigma.ClassNexa.model.response.QuestionsResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.QuestionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/questions")
public class QuestionsController {

    private final QuestionsService questionsService;

    @PostMapping
    public ResponseEntity<?> createNewQuestions(@RequestBody QuestionsRequest request) {
        QuestionsResponse questions = questionsService.create(request);
        WebResponse<QuestionsResponse> response = WebResponse.<QuestionsResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully create new Questions")
                .data(questions)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionsById(@PathVariable String id) {
        QuestionsResponse customerResponse = questionsService.getOne(id);
        WebResponse<QuestionsResponse> webResponse = WebResponse.<QuestionsResponse>builder()
                .message("successfully get Questions by id")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(customerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody QuestionsRequest request){
        QuestionsResponse update = questionsService.update(request);

        WebResponse<QuestionsResponse> response = WebResponse.<QuestionsResponse>builder()
                .message("Succesfully update status Questions")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(update)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllQuestions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String participantName,
            @RequestParam(required = false) String classeName

    ) {

        SearchQuestionsRequest request = SearchQuestionsRequest.builder()
                .page(page)
                .size(size)
                .participantName(participantName)
                .classeName(classeName)
                .build();

        Page<QuestionsResponse> responses = questionsService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPage(responses.getTotalPages())
                .totalElements(responses.getTotalElements())
                .build();

        WebResponse<List<QuestionsResponse>> response = WebResponse.<List<QuestionsResponse>>builder()
                .message("successfully get all questions")
                .status(HttpStatus.OK.getReasonPhrase())
                .pagging(pagingResponse)
                .data(responses.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionsById(@PathVariable String id) {
        questionsService.deleteById(id);
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
