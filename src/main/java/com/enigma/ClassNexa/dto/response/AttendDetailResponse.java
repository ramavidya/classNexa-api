package com.enigma.ClassNexa.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendDetailResponse {
    private String ParticipantId;
    private String participantName;
    private String info;
}
