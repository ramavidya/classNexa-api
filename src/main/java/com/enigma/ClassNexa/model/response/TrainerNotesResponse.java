package com.enigma.ClassNexa.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerNotesResponse {
    private String id;
    private String notes;
    private String trainer_id;
    private String trainer;
    private String schedule_id;
    private String start_class;
    private String end_class;
}
