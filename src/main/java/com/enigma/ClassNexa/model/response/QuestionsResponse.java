package com.enigma.ClassNexa.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startClasses;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endClasses;

    private List<ParticipantQuestionsResponse> participantQuestions;
}
