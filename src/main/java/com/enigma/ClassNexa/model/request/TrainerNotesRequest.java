package com.enigma.ClassNexa.model.request;

import com.enigma.ClassNexa.entity.Schedule;
import com.enigma.ClassNexa.entity.Trainer;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerNotesRequest {
    private String id;
    private String notes;
    private String trainer;
    private String schedule;
}
