package com.enigma.ClassNexa.controller;

import com.enigma.classnexa.model.request.UserUpdateRequest;
import com.enigma.classnexa.model.response.UserResponse;
import com.enigma.classnexa.model.response.WebResponse;
import com.enigma.classnexa.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/trainer")
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<UserResponse> getAll = trainerService.getAll();

        WebResponse<List<UserResponse>> response = WebResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get trainer")
                .data(getAll)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        UserResponse getById = trainerService.getById(id);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get trainer")
                .data(getById)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest request){
        UserResponse updateResponse = trainerService.update(request);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update trainer")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

}
