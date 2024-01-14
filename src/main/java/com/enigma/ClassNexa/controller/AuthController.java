package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.model.request.LoginRequest;
import com.enigma.ClassNexa.model.request.RegisterRequest;
import com.enigma.ClassNexa.model.response.CommonResponse;
import com.enigma.ClassNexa.model.response.RegisterResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(path = "/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request){
        RegisterResponse registerResponse = authService.registerAdmin(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly create new admin")
                .data(registerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register/trainer")
    public ResponseEntity<?> registerTrainer(@RequestBody RegisterRequest request) throws IOException {
        RegisterResponse registerResponse = authService.registerTrainer(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly create new trainer")
                .data(registerResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register/participant")
    public ResponseEntity<?> registerParticipan(@RequestBody RegisterRequest request) throws IOException {
        RegisterResponse registerResponse = authService.registerParticipant(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly create new participant")
                .data(registerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register/participant/upload",consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerUploadCsVParticipan(@RequestPart("file") MultipartFile file) throws IOException {
        Integer registerResponse = authService.uploadParticipant(file);
        CommonResponse<Integer> response = CommonResponse.<Integer>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly upload participant account")
                .data(registerResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        String login = authService.login(request);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly login")
                .data(login)
                .build();
        return ResponseEntity.ok(response);
    }
}
