package com.enigma.ClassNexa.model.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleAttendResponse {
    private String scheduleId;
    private Date classStartedAt;
    private AttendDetailResponse attendDetailResponse;
}
