package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "t_trainer_notes")
public class TrainerNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String notes;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
