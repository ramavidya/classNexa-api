package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/trainer")
public class TrainerController {

    private final TrainerService trainerService;

    @PreAuthorize("hasRole('TRAINER')")
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

    @PreAuthorize("hasRole('TRAINER')")
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

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ProfileUpdateRequest request){
        UserResponse updateResponse = trainerService.update(request);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update trainer")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping(path = "/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request){
        String updateResponse = trainerService.updatePassword(request);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update password")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String delete = trainerService.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly delete trainer")
                .data(delete)
                .build();

        return ResponseEntity.ok(response);
    }
}
