package com.enigma.ClassNexa.modul.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAttendRequest {
    private String id;
    private String participantId;
    private String categoryId;
    private String scheduleId;
}
