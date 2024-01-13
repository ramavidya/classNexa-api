package com.enigma.ClassNexa.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendDetailRequest {
    private String participantId;
    private String categoryId;
    private String absentReasons;
}
