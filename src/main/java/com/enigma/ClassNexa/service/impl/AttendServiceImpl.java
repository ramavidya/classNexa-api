package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.entity.Participant;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.response.AttendResponse;
import com.enigma.ClassNexa.repository.AttendRepository;
import com.enigma.ClassNexa.service.AttendService;
import com.enigma.ClassNexa.service.AttendanceService;
import com.enigma.ClassNexa.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService {
    private final AttendRepository attendRepository;
    private final AttendanceService attendanceService;
    private final ParticipantService participantService;
    @Override
    public Attend getAttendById(String id) {
        Optional<Attend> optionalAttend = attendRepository.findById(id);
        if (optionalAttend.isEmpty()) throw new RuntimeException("not found");
        return optionalAttend.get();
    }

    @Override
    public AttendResponse create(AttendRequest request) {
        Attendance attendanceById = attendanceService.getAttendanceById(request.getAttendanceId());
        AttendResponse attendResponse = AttendResponse.builder()
                .attendance(attendanceById)
                .participant(participantService.getAllParticipant())
                .build();
        return attendResponse;
    }

}
