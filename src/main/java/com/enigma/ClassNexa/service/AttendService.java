package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.dto.request.AttendRequest;
import com.enigma.ClassNexa.dto.request.SearchAttendRequest;
import com.enigma.ClassNexa.dto.request.UpdateAttendRequest;
import com.enigma.ClassNexa.dto.response.AttendResponse;
import com.enigma.ClassNexa.dto.response.SingleAttendResponse;

import java.util.List;

public interface AttendService {
    SingleAttendResponse getAttendById (String id);
    AttendResponse create(AttendRequest request);
    List<SingleAttendResponse> getAll(SearchAttendRequest request);
    void deleteById(String id);
    SingleAttendResponse Update(UpdateAttendRequest request);

}
