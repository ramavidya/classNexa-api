package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Trainer;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.request.SearchUserRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.response.CommonResponse;
import com.enigma.ClassNexa.model.response.PagingResponse;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/trainer")
public class TrainerController {

    private final TrainerService trainerService;

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size){
        SearchUserRequest buildSearch = SearchUserRequest.builder().name(name).page(page).size(size).build();
        Page<Trainer> pageTrainer = trainerService.getAll(buildSearch);
        List<UserResponse> userGetResponses = new ArrayList<>();
        for (Trainer trainer : pageTrainer.getContent()) {
            UserResponse buildResponse = UserResponse.builder().id(trainer.getId())
                    .name(trainer.getName())
                    .gender(trainer.getGender())
                    .address(trainer.getAddress())
                    .email(trainer.getUserCredential().getEmail())
                    .phoneNumber(trainer.getPhoneNumber()).build();
            userGetResponses.add(buildResponse);
        }
        WebResponse<List<UserResponse>> response = WebResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get trainer")
                .pagingResponse(new PagingResponse(pageTrainer.getTotalElements(),
                        pageTrainer.getTotalPages(),
                        pageTrainer.getNumber()+1,
                        pageTrainer.getSize()))
                .data(userGetResponses).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        UserResponse getById = trainerService.getById(id);
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
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
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
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
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update password")
                .data(updateResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String delete = trainerService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly delete trainer")
                .data(delete)
                .build();
        return ResponseEntity.ok(response);
    }
}
