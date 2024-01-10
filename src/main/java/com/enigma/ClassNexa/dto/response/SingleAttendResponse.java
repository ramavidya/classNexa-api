package com.enigma.ClassNexa.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleAttendResponse {
    private String id;
    private String scheduleId;
    private Timestamp classStartedAt;
    private AttendDetailResponse attendDetailResponse;
}
