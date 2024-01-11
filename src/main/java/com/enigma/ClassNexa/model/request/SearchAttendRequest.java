package com.enigma.ClassNexa.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAttendRequest {
    private Integer page;
    private Integer size;
    private String scheduleId;
    private String participantId;
}
