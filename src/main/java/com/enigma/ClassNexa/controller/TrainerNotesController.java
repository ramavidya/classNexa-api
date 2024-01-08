package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.TrainerNotes;
import com.enigma.ClassNexa.model.ScheduleRequest;
import com.enigma.ClassNexa.model.ScheduleResponse;
import com.enigma.ClassNexa.model.TrainerNotesRequest;
import com.enigma.ClassNexa.model.WebResponse;
import com.enigma.ClassNexa.service.TrainerNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TrainerNotesController {
    private final TrainerNotesService trainerNotesService;

    @PostMapping(path = "/api/notes")
    public ResponseEntity<?> create(@RequestBody TrainerNotesRequest request){
        TrainerNotes trainerNotes = trainerNotesService.create(request);
        WebResponse<TrainerNotes> response = WebResponse.<TrainerNotes>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Data Created")
                .data(trainerNotes)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/api/notes")
    public ResponseEntity<?> getAll(){
        List<TrainerNotes> all = trainerNotesService.getAll();
        WebResponse<List<TrainerNotes>> response = WebResponse.<List<TrainerNotes>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("All data schedule")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/api/notes/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        TrainerNotes all = trainerNotesService.getById(id);
        WebResponse<TrainerNotes> response = WebResponse.<TrainerNotes>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Get data schedule by id")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/api/notes")
    public ResponseEntity<?> update(@RequestBody TrainerNotesRequest request){
        TrainerNotes schedule = trainerNotesService.update(request);
        WebResponse<TrainerNotes> response = WebResponse.<TrainerNotes>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Data Updated")
                .data(schedule)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(path = "/api/notes/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        String all = trainerNotesService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Data deleted")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
