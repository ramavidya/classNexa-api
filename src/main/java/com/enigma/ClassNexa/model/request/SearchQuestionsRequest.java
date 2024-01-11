package com.enigma.ClassNexa.model.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;


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
    private String classesName;
    private String trainerName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date start_class;
    private boolean status;
}
