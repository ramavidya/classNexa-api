package com.enigma.ClassNexa.dto.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendResponse {
    private String id;

    private String scheduleId;
    private Timestamp classStartedAt;

    private List<AttendDetailResponse> attendDetailResponses;
}
