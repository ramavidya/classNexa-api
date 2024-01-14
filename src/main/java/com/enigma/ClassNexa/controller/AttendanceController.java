package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.model.response.WebResponse;
import com.enigma.ClassNexa.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping(path = "/api/attendance/{id}")
    @PreAuthorize("hasAnyRole('TRAINER','ADMIN')")
    public ResponseEntity<?> getById(@PathVariable String id){
        Attendance attendanceById = attendanceService.getAttendanceById(id);
        WebResponse<Attendance> response = WebResponse.<Attendance>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(attendanceById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping(path = "/api/attendance")
    @PreAuthorize("hasAnyRole('TRAINER','ADMIN')")
    public ResponseEntity<?> getAll(){
        WebResponse<List<Attendance>> response = WebResponse.<List<Attendance>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(attendanceService.getAllAttendance())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping(path = "/api/attendance")
    @PreAuthorize("hasAnyRole('TRAINER','ADMIN')")
    public ResponseEntity<?> createAttendance(@RequestBody Attendance request){
        Attendance attendance = attendanceService.create(request);
        WebResponse<Attendance> response = WebResponse.<Attendance>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("success")
                .data(attendance)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping(path = "/api/attendance/{id}")
    @PreAuthorize("hasAnyRole('TRAINER','ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        attendanceService.deleteAttendance(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data("OK")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
