package com.enigma.ClassNexa.model.request;


import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchQuestionsRequest {
    private String participantId;
    private String statusId;
    private String questionsId;
    private Integer page;
    private Integer size;
    private String participantName;
    private String classeName;
    private String trainerName;
    private LocalDateTime start_class;
    private boolean status;
}
