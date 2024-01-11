package com.enigma.ClassNexa.modul.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendDetailRequest {
    private String participantId;
    private String categoryId;
}
