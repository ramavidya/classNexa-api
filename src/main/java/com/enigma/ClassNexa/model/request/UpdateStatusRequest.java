package com.enigma.ClassNexa.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusRequest {
    private String questionsId;
    private String scheduleId;
    private boolean status;
}
