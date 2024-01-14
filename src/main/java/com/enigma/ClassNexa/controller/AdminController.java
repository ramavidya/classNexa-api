package com.enigma.ClassNexa.controller;


import com.enigma.ClassNexa.entity.Admin;
import com.enigma.ClassNexa.model.request.ProfileUpdateRequest;
import com.enigma.ClassNexa.model.request.SearchUserRequest;
import com.enigma.ClassNexa.model.request.UpdatePasswordRequest;
import com.enigma.ClassNexa.model.response.CommonResponse;
import com.enigma.ClassNexa.model.response.PagingResponse;
import com.enigma.ClassNexa.model.response.UserResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.AdminService;
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
@RequestMapping(path = "/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size){
        SearchUserRequest buildSearch = SearchUserRequest.builder()
                                        .name(name).page(page).size(size).build();
        Page<Admin> pageAdmin = adminService.getAll(buildSearch);
        List<UserResponse> userGetResponses = new ArrayList<>();
        for (Admin admin : pageAdmin.getContent()) {
            UserResponse buildResponse = UserResponse.builder().id(admin.getId())
                    .name(admin.getName())
                    .gender(admin.getGender())
                    .address(admin.getAddress())
                    .email(admin.getUserCredential().getEmail())
                    .phoneNumber(admin.getPhoneNumber()).build();
            userGetResponses.add(buildResponse);
        }
        WebResponse<List<UserResponse>> response = WebResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly get admin")
                .pagingResponse(new PagingResponse(pageAdmin.getTotalElements(),
                        pageAdmin.getTotalPages(),
                        pageAdmin.getNumber()+1,
                        pageAdmin.getSize()))
                .data(userGetResponses).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        UserResponse getByIdResponse = adminService.getById(id);
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
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
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
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
        String delete = adminService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly delete admin")
                .data(delete)
                .build();
        return ResponseEntity.ok(response);
    }
}
