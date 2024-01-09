package com.enigma.ClassNexa.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsRequest {

    private String question;
    private String course;
    private String chapter;
    private String statusId;
    private String participantId;
    private String scheduleId;

}




