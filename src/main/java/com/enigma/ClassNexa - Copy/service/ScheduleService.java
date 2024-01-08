package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.model.ScheduleRequest;
import com.enigma.ClassNexa.model.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    ScheduleResponse create(ScheduleRequest request);
    List<ScheduleResponse> getAll();
    String delete(String id);
    ScheduleResponse update(ScheduleRequest request);
    ScheduleResponse getById(String id);
    Schedule getByIdSchedule(String id);
}
