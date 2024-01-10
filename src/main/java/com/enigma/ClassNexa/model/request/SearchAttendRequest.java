package com.enigma.ClassNexa.model.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAttendRequest {
    private Integer page;
    private Integer size;
    private Timestamp classStartedAt;
    private String participantName;
}
