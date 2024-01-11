package com.enigma.ClassNexa.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionsStatusResponse {
    private String id;
    private boolean status;

}
