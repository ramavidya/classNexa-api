package com.enigma.ClassNexa.modul.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendResponse {
    private String id;

    private String scheduleId;
    private Date classStartedAt;

    private List<AttendDetailResponse> attendDetailResponses;
}
