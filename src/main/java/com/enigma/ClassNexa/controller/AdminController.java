package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ProfileUpdateRequest request){
        UserResponse updateResponse = adminService.update(request);

        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update admin")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request){
        String updateResponse = adminService.updatePassword(request);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly update password")
                .data(updateResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String delete = adminService.delete(id);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly delete admin")
                .data(delete)
                .build();

        return ResponseEntity.ok(response);
    }
}
