package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.model.request.ScheduleRequest;
import com.enigma.ClassNexa.model.response.ScheduleResponse;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping(path = "/api/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody ScheduleRequest request){
        ScheduleResponse schedule = scheduleService.create(request);
        WebResponse<ScheduleResponse> response = WebResponse.<ScheduleResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Data Created")
                .data(schedule)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/api/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(){
        List<ScheduleResponse> all = scheduleService.getAll();
        WebResponse<List<ScheduleResponse>> response = WebResponse.<List<ScheduleResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("All data schedule")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(path = "/api/schedule/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id){
        String all = scheduleService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Data deleted")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/api/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody ScheduleRequest request){
        ScheduleResponse schedule = scheduleService.update(request);
        WebResponse<ScheduleResponse> response = WebResponse.<ScheduleResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Data Updated")
                .data(schedule)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/api/schedule/{id}")
    @PreAuthorize("hasAnyRole('PARTICIPANT','TRAINER')")
    public ResponseEntity<?> getById(@PathVariable String id){
        ScheduleResponse all = scheduleService.getById(id);
        WebResponse<ScheduleResponse> response = WebResponse.<ScheduleResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Get data schedule by id")
                .data(all)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
