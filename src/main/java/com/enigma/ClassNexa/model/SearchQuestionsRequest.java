package com.enigma.ClassNexa.model;

import lombok.*;

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
}
