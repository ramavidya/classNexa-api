package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.ParticipantRama;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.ParticipantServiceRama;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantControllerRama {
    private final ParticipantServiceRama participantServiceRama;
    @GetMapping(path = "/api/participant/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        ParticipantRama participantRamaById = participantServiceRama.getParticipantById(id);
        WebResponse<ParticipantRama> response = WebResponse.<ParticipantRama>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(participantRamaById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping(path = "/api/participant")
    public ResponseEntity<?> getAll(){
        List<ParticipantRama> allParticipantRama = participantServiceRama.getAllParticipant();
        WebResponse<List<ParticipantRama>> response = WebResponse.<List<ParticipantRama>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(allParticipantRama)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
