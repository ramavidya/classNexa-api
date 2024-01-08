package com.enigma.ClassNexa.model;


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
    private Question_Status_Request status;
    private String participantId;
    private String ClassId;
    private String scheduleId;

}




