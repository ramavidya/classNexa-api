package com.enigma.ClassNexa.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsRequest {

    private String id;
    private String question;
    private String course;
    private String chapter;
    private QuestionStatusRequest status;
    private String participantId;
    private String ClassId;
    private String scheduleId;

}




