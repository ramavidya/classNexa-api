package com.enigma.ClassNexa.model.request;

import com.enigma.ClassNexa.entity.Questions_Status;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchQuestionsRequest {
    private Integer page;
    private Integer size;
    private String participantName;
    private String classeName;
    private String trainerName;
}
