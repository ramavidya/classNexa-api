package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Attendance;

import java.util.List;

public interface AttendanceService {
    Attendance getAttendanceById (String id);
    Attendance create(Attendance request);

    List<Attendance> getAllAttendance();

    void deleteAttendance(String id);
}
