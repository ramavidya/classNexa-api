package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.repository.AttendanceRepository;
import com.enigma.ClassNexa.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attendance getAttendanceById(String id) {
         return attendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attendance create(Attendance request) {
        Attendance attendance = Attendance.builder()
                .category(request.getCategory())
                .build();
        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttendance(String id) {
        attendanceRepository.deleteById(id);
    }

}
