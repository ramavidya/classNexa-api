package com.enigma.ClassNexa.modul.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendRequest {
    private String scheduleId;
    private List<AttendDetailRequest> attendDetailRequests;
}
