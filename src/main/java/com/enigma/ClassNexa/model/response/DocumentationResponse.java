package com.enigma.ClassNexa.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentationResponse {
    private String id;
    private String filename;
    private String triner_id;
    private String trainer;
    private String schedule_id;
    private String date;
}
