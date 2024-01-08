package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Attend;
import com.enigma.ClassNexa.model.request.AttendRequest;
import com.enigma.ClassNexa.model.response.AttendResponse;

public interface AttendService {
    Attend getAttendById (String id);
    AttendResponse create(AttendRequest request);
}
