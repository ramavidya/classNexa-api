package com.enigma.ClassNexa.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionStatusRequest {
    private String id;
    private boolean status;
    private Integer page;
    private Integer size;
}
