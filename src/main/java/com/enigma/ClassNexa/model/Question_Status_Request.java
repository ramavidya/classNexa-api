package com.enigma.ClassNexa.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question_Status_Request {
    private String id;
    private boolean status;
    private Integer page;
    private Integer size;
}
