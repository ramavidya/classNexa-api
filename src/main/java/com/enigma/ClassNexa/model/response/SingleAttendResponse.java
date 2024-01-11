package com.enigma.ClassNexa.model.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleAttendResponse {
    private String scheduleId;
    private Timestamp classStartedAt;
    private AttendDetailResponse attendDetailResponse;
}
