package com.enigma.ClassNexa.dto.request;

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
