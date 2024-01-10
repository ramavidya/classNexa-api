package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.dto.response.WebResponse;
import com.enigma.ClassNexa.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;
    @GetMapping(path = "/api/participant/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Participant participantById = participantService.getParticipantById(id);
        WebResponse<Participant> response = WebResponse.<Participant>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(participantById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping(path = "/api/participant")
    public ResponseEntity<?> getAll(){
        List<Participant> allParticipant = participantService.getAllParticipant();
        WebResponse<List<Participant>> response = WebResponse.<List<Participant>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(allParticipant)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
