package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.repository.AttendanceRepository;
import com.enigma.ClassNexa.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    @Override
    public Attendance getAttendanceById(String id) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(id);
        if (optionalAttendance.isEmpty()) throw new RuntimeException("not found");
        return optionalAttendance.get();
    }

    @Override
    public Attendance create(Attendance request) {
        Attendance attendance = Attendance.builder()
                .category(request.getCategory())
                .build();
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance getAllAttendance() {
        return null;
    }

}
