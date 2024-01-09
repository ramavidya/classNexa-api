package com.enigma.ClassNexa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "t_questions")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String question;

    private String course;

    private String chapter;

    @ManyToOne
    @JoinColumn(name = "questions_status_id")
    private Questions_Status questionsStatus;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;



}
