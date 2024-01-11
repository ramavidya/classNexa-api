package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.modul.request.AttendRequest;
import com.enigma.ClassNexa.modul.request.SearchAttendRequest;
import com.enigma.ClassNexa.modul.request.UpdateAttendRequest;
import com.enigma.ClassNexa.modul.response.AttendResponse;
import com.enigma.ClassNexa.modul.response.SingleAttendResponse;

import java.util.List;

public interface AttendService {
    SingleAttendResponse getAttendById (String id);
    AttendResponse create(AttendRequest request);
    List<SingleAttendResponse> getAll(SearchAttendRequest request);
    void deleteById(String id);
    SingleAttendResponse Update(UpdateAttendRequest request);

}
