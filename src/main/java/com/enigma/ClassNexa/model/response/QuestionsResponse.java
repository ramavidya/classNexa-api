package com.enigma.ClassNexa.model.response;

import lombok.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsResponse {
    private String questionsId;
    private String className;
    private String trainerName;
    private LocalDateTime startClasses;
    private LocalDateTime endClasses;

    private List<ParticipantQuestionsResponse> participantQuestions;
}
