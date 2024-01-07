package com.enigma.ClassNexa.controller;

import com.enigma.classnexa.model.request.LoginRequest;
import com.enigma.classnexa.model.request.RegisterRequest;
import com.enigma.classnexa.model.response.RegisterResponse;
import com.enigma.classnexa.model.response.WebResponse;
import com.enigma.classnexa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(path = "/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request){
        RegisterResponse registerResponse = authService.registerAdmin(request);
        WebResponse<RegisterResponse> response = WebResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly create new admin")
                .data(registerResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register/trainer")
    public ResponseEntity<?> registerTrainer(@RequestBody RegisterRequest request){
        RegisterResponse registerResponse = authService.registerTrainer(request);
        WebResponse<RegisterResponse> response = WebResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly create new trainer")
                .data(registerResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register/participant")
    public ResponseEntity<?> registerParticipan(@RequestBody RegisterRequest request){
        RegisterResponse registerResponse = authService.registerParticipant(request);
        WebResponse<RegisterResponse> response = WebResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfuly create new participant")
                .data(registerResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        String login = authService.login(request);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfuly login")
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }
}
