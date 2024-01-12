package com.enigma.ClassNexa.service.impl;

import com.enigma.ClassNexa.entity.Attendance;
import com.enigma.ClassNexa.repository.AttendanceRepository;
import com.enigma.ClassNexa.service.AttendanceService;
import com.enigma.ClassNexa.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ValidationUtils validationUtils;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attendance getAttendanceById(String id) {
         return attendanceRepository.findById(id)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attendance create(Attendance request) {
        validationUtils.validate(request);
        if (request.getCategory() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot fill in blank");
        Attendance attendance = Attendance.builder()
                .id(request.getId())
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
