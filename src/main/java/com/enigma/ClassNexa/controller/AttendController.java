package com.enigma.ClassNexa.controller;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.model.WebResponse;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.response.AttendResponse;
import com.enigma.ClassNexa.service.AttendService;
import com.enigma.ClassNexa.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;
    private final AttendanceService attendanceService;
    @GetMapping(path = "/api/attend/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Attend attendById = attendService.getAttendById(id);
        WebResponse<Attend> response = WebResponse.<Attend>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("success")
                .data(attendById)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping(path = "/api/attend")
    public ResponseEntity<?> createAttend(@RequestBody AttendRequest request){
        AttendResponse attendResponse = attendService.create(request);
        WebResponse<AttendResponse> response = WebResponse.<AttendResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("success")
                .data(attendResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED ).body(response);
    }
}
