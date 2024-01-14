package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.TrainerNotes;
import com.enigma.ClassNexa.model.*;
import com.enigma.ClassNexa.model.request.SearchTrainerNotesRequest;
import com.enigma.ClassNexa.model.request.TrainerNotesRequest;
import com.enigma.ClassNexa.model.response.PagingResponse;
import com.enigma.ClassNexa.model.response.TrainerNotesResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.TrainerNotesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TrainerNotesController {
    private final TrainerNotesService trainerNotesService;

    @PostMapping(path = "/api/notes")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> create(@RequestBody TrainerNotesRequest request){
        TrainerNotesResponse trainerNotes = trainerNotesService.create(request);
        WebResponse<TrainerNotesResponse> response = WebResponse.<TrainerNotesResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Data Created")
                .data(trainerNotes)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/api/notes")
    @PreAuthorize("hasAnyRole('TRAINER','PARTICIPANT')")
    public ResponseEntity<?> getAll(){
        List<TrainerNotesResponse> all = trainerNotesService.getAll();
        WebResponse<List<TrainerNotesResponse>> response = WebResponse.<List<TrainerNotesResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("All data schedule")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/api/notes/{id}")
    @PreAuthorize("hasAnyRole('TRAINER','PARTICIPANT')")
    public ResponseEntity<?> getById(@PathVariable String id){
        TrainerNotesResponse all = trainerNotesService.getById(id);
        WebResponse<TrainerNotesResponse> response = WebResponse.<TrainerNotesResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Get data schedule by id")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/api/notes")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> update(@RequestBody TrainerNotesRequest request){
        TrainerNotesResponse schedule = trainerNotesService.update(request);
        WebResponse<TrainerNotesResponse> response = WebResponse.<TrainerNotesResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Data Updated")
                .data(schedule)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(path = "/api/notes/{id}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<?> delete(@PathVariable String id){
        String all = trainerNotesService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Data deleted")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/api/notes/param")
    @PreAuthorize("hasAnyRole('TRAINER','PARTICIPANT')")
    public ResponseEntity<?> getAllTrainerNotes(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String schedulee,
            @RequestParam(required = false) String classes

    ) {

        SearchTrainerNotesRequest request = SearchTrainerNotesRequest.builder()
                .page(page)
                .size(size)
                .classes(classes)
                .schedule(schedulee)
                .build();

        Page<TrainerNotes> products = trainerNotesService.getAllTrainerNotes(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.getPage())
                .size(size)
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .build();

        List<TrainerNotesResponse> trainerNotesResponses = new ArrayList<>();
        for(TrainerNotes trainerNotes : products.getContent()){

            TrainerNotesResponse trainerNotesResponse = TrainerNotesResponse.builder()
                    .id(trainerNotes.getId())
                    .notes(trainerNotes.getNotes())
                    .trainer_id(trainerNotes.getTrainer().getId())
                    .trainer(trainerNotes.getTrainer().getName())
                    .schedule_id(trainerNotes.getSchedule().getId())
                    .start_class(trainerNotes.getSchedule().getStart_class().toString())
                    .end_class(trainerNotes.getSchedule().getEnd_class().toString())
                    .build();
            trainerNotesResponses.add(trainerNotesResponse);
        }

        WebResponse<List<TrainerNotesResponse>> response = WebResponse.<List<TrainerNotesResponse>>builder()
                .message("successfully get all product")
                .status(HttpStatus.OK.getReasonPhrase())
                .data(trainerNotesResponses)
                .build();

        return ResponseEntity.ok(response);
    }

}
