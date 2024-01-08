package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.model.WebResponse;
import com.enigma.ClassNexa.service.ClassesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClassesController {
    private final ClassesService classesService;
    @GetMapping(path = "/api/classes/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Classes classById = classesService.getClassById(id);
        WebResponse<Classes> response = WebResponse.<Classes>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(classById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
