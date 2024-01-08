package com.enigma.ClassNexa.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsResponse {
    private String id;
    private String className;
    private String trainerName;
    private String participant_name;
    private String question;
    private String course;
    private String chapter;
    private LocalDateTime start_clases;
    private LocalDateTime end_classes;
    private String status;

}
