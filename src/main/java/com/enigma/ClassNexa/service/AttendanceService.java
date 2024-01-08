package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Attendance;

public interface AttendanceService {
    Attendance getAttendanceById (String id);
    Attendance create(Attendance request);

    Attendance getAllAttendance();
}
