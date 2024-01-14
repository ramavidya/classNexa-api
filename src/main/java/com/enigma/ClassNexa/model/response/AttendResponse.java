package com.enigma.ClassNexa.model.response;

import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendResponse {

    private String scheduleId;
    private Date classStartedAt;

    private List<AttendDetailResponse> attendDetailResponses;
}
