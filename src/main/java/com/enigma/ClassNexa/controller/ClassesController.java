package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.dto.Request.ClassesRequest;
import com.enigma.ClassNexa.dto.Request.SearchDetailClassRequest;
import com.enigma.ClassNexa.dto.Request.UpdateClassesRequest;
import com.enigma.ClassNexa.dto.Response.ClassResponse;
import com.enigma.ClassNexa.dto.Response.PagingResponse;
import com.enigma.ClassNexa.dto.Response.WebResponse;
import com.enigma.ClassNexa.service.ClassDetailService;
import com.enigma.ClassNexa.service.ClassesService;
import com.enigma.ClassNexa.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/classses")
public class ClassesController {

    private final ClassesService classesService;
    private  final ClassDetailService classDetailService;

    @PostMapping
    public ResponseEntity<?> createNew(@RequestBody ClassesRequest request){
        ClassResponse classResponse = classesService.create(request);
        WebResponse<ClassResponse> response = WebResponse.<ClassResponse>builder()
                .message("Success Create Class Room")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .data(classResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateClassesRequest request){
        ClassResponse classResponse = classesService.update(request);
        WebResponse<ClassResponse> response = WebResponse.<ClassResponse>builder()
                .message("Success Update Class Room")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(classResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        classesService.deleteById(id);
        WebResponse<String> webResponse = WebResponse.<String>builder()
                .message("Success Delete Class Room")
                .status(HttpStatus.OK.getReasonPhrase())
                .data("OK")
                .build();
        return ResponseEntity.ok(webResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        ClassResponse classResponse = classesService.getById(id);
        WebResponse<ClassResponse> response = WebResponse.<ClassResponse>builder()
                .message("Success Get Class Room")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(classResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam(required = false, defaultValue = "asc") String direction,
                                    @RequestParam(required = false, defaultValue = "name") String sortBy,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String trainer){
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchDetailClassRequest searchDetailClassRequest = SearchDetailClassRequest.builder()
                .classes(name)
                .direction(direction)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .trainer(trainer)
                .build();
        Page<ClassResponse> classResponses = classesService.getAll(searchDetailClassRequest);
        PagingResponse pagingResponse = PagingResponse.builder()
                .page(searchDetailClassRequest.getPage())
                .size(size)
                .totalElements(classResponses.getTotalElements())
                .totalPages(classResponses.getTotalPages())
                .build();

        WebResponse<List<ClassResponse>> response = WebResponse.<List<ClassResponse>>builder()
                .message("Success Get Class Room")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(classResponses.getContent())
                .pagingResponse(pagingResponse)
                .build();
        return ResponseEntity.ok(response);
    }

}
