package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.model.request.QuestionsRequest;
import com.enigma.ClassNexa.model.request.SearchQuestionsRequest;
import com.enigma.ClassNexa.model.request.UpdateStatusRequest;
import com.enigma.ClassNexa.model.response.PagingResponse;
import com.enigma.ClassNexa.model.response.QuestionsResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.QuestionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @PreAuthorize("hasAnyRole('ADMIN','TRAINER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateStatusRequest request){
        QuestionsResponse update = questionsService.update(request);

        WebResponse<QuestionsResponse> response = WebResponse.<QuestionsResponse>builder()
                .message("Succesfully update status Questions")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(update)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TRAINER')")
    @GetMapping
    public ResponseEntity<?> getAllQuestions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String participantName,
            @RequestParam(required = false) String classesName,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start_classes,
            @RequestParam(required = false) boolean status
    ) {


        SearchQuestionsRequest request = SearchQuestionsRequest.builder()
                .page(page)
                .size(size)
                .classesName(classesName)
                .trainerName(trainerName)
                .participantName(participantName)
                .start_class(start_classes)
                .status(status)
                .build();

        Page<QuestionsResponse> responses = questionsService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(responses.getTotalPages())
                .totalElements(responses.getTotalElements())
                .build();


        WebResponse<List<QuestionsResponse>> response = WebResponse.<List<QuestionsResponse>>builder()
                .message("successfully get all questions")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(responses.getContent())
                .pagingResponse(pagingResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PreAuthorize("hasAnyRole('ADMIN','TRAINER')")
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
