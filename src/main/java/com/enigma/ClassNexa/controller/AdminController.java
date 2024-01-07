package com.enigma.ClassNexa.controller;


import com.enigma.classnexa.model.request.UserUpdateRequest;
import com.enigma.classnexa.model.response.UserResponse;
import com.enigma.classnexa.model.response.WebResponse;
import com.enigma.classnexa.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<UserResponse> getAllResponse = adminService.getAll();

        WebResponse<List<UserResponse>> response = WebResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get admin")
                .data(getAllResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        UserResponse getByIdResponse = adminService.getById(id);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get admin")
                .data(getByIdResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest request){
        UserResponse updateResponse = adminService.update(request);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update admin")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }


}
